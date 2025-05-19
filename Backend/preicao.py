import os
import joblib
import requests
from flask import Flask, request, jsonify

app = Flask(__name__)

# --- URLs e chave da API ---
ORS_API_KEY = "5b3ce3597851110001cf624855631f968bc045de9321e2911cf0396f"
ORS_GEOCODE_URL = "https://api.openrouteservice.org/geocode/search"
ORS_DIRECTIONS_URL = "https://api.openrouteservice.org/v2/directions/driving-car"

# --- Carrega todos os modelos na pasta modelos/ ---
def carregar_modelos():
    modelos = {}
    modelos_path = os.path.join(os.getcwd(), "modelos")
    for arquivo in os.listdir(modelos_path):
        if arquivo.endswith(".pkl"):
            categoria = arquivo.replace("model_", "").replace(".pkl", "")
            modelos[categoria] = joblib.load(os.path.join(modelos_path, arquivo))
    return modelos

# --- Geocodifica um endereço para latitude e longitude ---
def geocode(endereco):
    params = {
        "api_key": ORS_API_KEY,
        "text": endereco,
        "size": 1
    }
    response = requests.get(ORS_GEOCODE_URL, params=params)
    print("RESPONSE JSON:", response.status_code, response.text)  # log útil
    if response.status_code == 200:
        features = response.json().get("features")
        if features:
            coords = features[0]["geometry"]["coordinates"]
            return coords[1], coords[0]  # retorna (lat, lon)
    return None, None

# --- Usa API de rotas para calcular distância e duração ---
def calcular_rota(lat1, lon1, lat2, lon2):
    headers = {"Authorization": ORS_API_KEY, "Content-Type": "application/json"}
    body = {"coordinates": [[lon1, lat1], [lon2, lat2]]}
    response = requests.post(ORS_DIRECTIONS_URL, json=body, headers=headers)

    print("RESPONSE DIRECTIONS:", response.status_code, response.text)

    if response.status_code == 200:
        try:
            segmento = response.json()["routes"][0]["summary"]
            distancia_km = segmento["distance"] / 1000
            duracao_min = segmento["duration"] / 60
            return round(distancia_km, 2), round(duracao_min, 2)
        except Exception as e:
            print("Erro ao processar rota:", e)
            return None, None
    return None, None



# --- Rota principal ---
@app.route("/", methods=["POST"])
def prever():
    try:
        data = request.json
        print("JSON recebido:", data)

        origem = data.get("origem")
        destino = data.get("destino")

        if not origem or not destino:
            return jsonify({"erro": "Campos obrigatórios: origem, destino"}), 400

        lat1, lon1 = geocode(origem)
        lat2, lon2 = geocode(destino)
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

        print("Preços estimados:", precos)

        return jsonify({
            "origem": origem,
            "destino": destino,
            "distancia_km": distancia,
            "duracao_min": duracao,
            "precos_estimados": precos
        })

    except Exception as e:
        print("Erro durante execução:", str(e))  # <- aqui você vai ver o erro no terminal
        return jsonify({"erro": str(e)}), 500


# --- Execução local ---
if __name__ == "__main__":
    app.run(debug=False, host="0.0.0.0", port=5000)