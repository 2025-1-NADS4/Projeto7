import os
import joblib
import requests
import time
from flask import Flask, request, jsonify
from functools import lru_cache

app = Flask(__name__)

GOOGLE_API_KEY = "AIzaSyBShc9opwOsghA-bmgNF2n3FdSNJSfY08U"
GOOGLE_GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json"
GOOGLE_DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json"

# --- Carrega modelos da pasta modelos/ ---
def carregar_modelos():
    modelos = {}
    modelos_path = os.path.join(os.getcwd(), "modelos")
    for arquivo in os.listdir(modelos_path):
        if arquivo.endswith(".pkl"):
            categoria = arquivo.replace("model_", "").replace(".pkl", "")
            modelos[categoria] = joblib.load(os.path.join(modelos_path, arquivo))
    return modelos

# --- Geocodifica um endereço (com cache) ---
@lru_cache(maxsize=100)
def geocode_cached(endereco):
    params = {"address": endereco, "key": GOOGLE_API_KEY}
    response = requests.get(GOOGLE_GEOCODE_URL, params=params)
    print("RESPONSE GEOCODE:", response.status_code, response.text)
    if response.status_code == 200:
        res_json = response.json()
        if res_json["status"] == "OK" and res_json["results"]:
            location = res_json["results"][0]["geometry"]["location"]
            return location["lat"], location["lng"]
    return None, None

# --- Calcula rota usando Google Directions ---
def calcular_rota(lat1, lon1, lat2, lon2):
    params = {
        "origin": f"{lat1},{lon1}",
        "destination": f"{lat2},{lon2}",
        "key": GOOGLE_API_KEY
    }
    response = requests.get(GOOGLE_DIRECTIONS_URL, params=params)
    print("RESPONSE DIRECTIONS:", response.status_code, response.text)

    if response.status_code == 200:
        res_json = response.json()
        try:
            route = res_json["routes"][0]["legs"][0]
            distancia_km = route["distance"]["value"] / 1000
            duracao_min = route["duration"]["value"] / 60
            return round(distancia_km, 2), round(duracao_min, 2)
        except Exception as e:
            print("Erro ao processar rota:", e)
            return None, None
    return None, None

# --- Rota principal ---
@app.route("/", methods=["POST"])
def prever():
    try:
        start = time.time()

        data = request.json
        print("JSON recebido:", data)

        origem = data.get("origem")
        destino = data.get("destino")

        if not origem or not destino:
            return jsonify({"erro": "Campos obrigatórios: origem, destino"}), 400

        lat1, lon1 = geocode_cached(origem)
        lat2, lon2 = geocode_cached(destino)
        print("Coordenadas:", lat1, lon1, lat2, lon2)

        if None in [lat1, lon1, lat2, lon2]:
            return jsonify({"erro": "Erro ao geocodificar os endereços"}), 400

        distancia, duracao = calcular_rota(lat1, lon1, lat2, lon2)
        print("Distância/duração:", distancia, duracao)

        if distancia is None:
            return jsonify({"erro": "Erro ao calcular rota"}), 500

        modelos = carregar_modelos()
        print("Modelos carregados:", list(modelos.keys()))

        precos = {
            categoria: round(model.predict([[distancia, duracao]])[0], 2)
            for categoria, model in modelos.items()
        }

        horas = int(duracao // 60)
        minutos = int(duracao % 60)
        tempo_formatado = f"{horas}h {minutos}min"

        # Criar lista de serviços no formato desejado
        servicos = []
        for tipo_transporte, preco in precos.items():
            servico = {
                "Rota": f"{origem} -> {destino}",
                "DistanciaKM": distancia,
                "TempoEstimado": tempo_formatado,
                "TipoTransporte": tipo_transporte.capitalize(),  # Ex: "uber" vira "Uber"
                "PrecoEstimado": preco
            }
            servicos.append(servico)

        return jsonify(servicos)  # Retorna diretamente a lista

    except Exception as e:
        print("Erro durante execução:", str(e))
        return jsonify({"erro": str(e)}), 500
# --- Execução local ---
if __name__ == "__main__":
    app.run(debug=False, host="0.0.0.0", port=5000)