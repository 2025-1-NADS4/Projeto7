import os
import json
import time
import joblib
import requests
from flask import Flask, request, jsonify
from flask_bcrypt import Bcrypt
from functools import lru_cache
from flask_cors import CORS







app = Flask(__name__)
bcrypt = Bcrypt(app)

CORS(app, origins=["http://10.0.2.2:5136/"])  # Substitua pelo endereço do seu frontend

ORS_API_KEY = "5b3ce3597851110001cf624871fe9e05c8254137897dc366758619a8"
ORS_GEOCODE_URL = "https://api.openrouteservice.org/geocode/search"
GOOGLE_API_KEY = "AIzaSyBdrPuqLvZrxCJ59kODrtElbaIx5JxpGCc"
GOOGLE_GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json"
GOOGLE_DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json"
USUARIOS_PATH = "usuarios.json"

# === USUÁRIOS ===
def carregar_usuarios():
    if os.path.exists(USUARIOS_PATH):
        with open(USUARIOS_PATH, "r") as f:
            return json.load(f)
    return []

@app.route("/registrar", methods=["POST"])
def registrar():
    data = request.json
    usuario = data.get("usuario")
    senha = data.get("senha")

    if not usuario or not senha:
        return jsonify({"erro": "Campos obrigatórios"}), 400

    usuarios = carregar_usuarios()
    for u in usuarios:
        if u["usuario"] == usuario:
            return jsonify({"erro": "Usuário já existe"}), 400

    senhaHash = bcrypt.generate_password_hash(senha).decode("utf-8")
    usuarios.append({"usuario": usuario, "senhaHash": senhaHash})

    with open(USUARIOS_PATH, "w") as f:
        json.dump(usuarios, f, indent=2)

    return jsonify({"mensagem": "Usuário registrado com sucesso"}), 201

@app.route("/login", methods=["POST"])
def login():
    data = request.json
    usuario = data.get("usuario")
    senha = data.get("senha")

    usuarios = carregar_usuarios()
    for u in usuarios:
        if u["usuario"] == usuario and bcrypt.check_password_hash(u["senhaHash"], senha):
            return jsonify({"mensagem": "Login bem-sucedido"}), 200

    return jsonify({"erro": "Usuário ou senha inválido"}), 401

# === PREDIÇÃO ===


@lru_cache(maxsize=100)
def geocode_cached(endereco):
    params = {
        "address": endereco,
        "key": GOOGLE_API_KEY
    }
    response = requests.get(GOOGLE_GEOCODE_URL, params=params)
    print("RESPONSE GOOGLE GEOCODE:", response.status_code, response.text)

    if response.status_code == 200:
        res_json = response.json()
        if res_json["results"]:
            location = res_json["results"][0]["geometry"]["location"]
            lat = location["lat"]
            lon = location["lng"]
            return lat, lon
    return None, None


def calcular_rota(lat1, lon1, lat2, lon2):
    params = {
        "origin": f"{lat1},{lon1}",
        "destination": f"{lat2},{lon2}",
        "key": GOOGLE_API_KEY
    }
    response = requests.get(GOOGLE_DIRECTIONS_URL, params=params)
    if response.status_code == 200:
        res_json = response.json()
        try:
            route = res_json["routes"][0]["legs"][0]
            distancia_km = route["distance"]["value"] / 1000
            duracao_min = route["duration"]["value"] / 60
            return round(distancia_km, 2), round(duracao_min, 2)
        except:
            return None, None
    return None, None

def carregar_modelos():
    modelos = {}
    modelos_path = os.path.join(os.getcwd(), "modelos")
    for arquivo in os.listdir(modelos_path):
        if arquivo.endswith(".pkl"):
            categoria = arquivo.replace("model_", "").replace(".pkl", "")
            modelos[categoria] = joblib.load(os.path.join(modelos_path, arquivo))
    return modelos

@app.route("/prever", methods=["POST"])
def prever():
    try:
        start = time.time()

        data = request.json
        origem = data.get("origem")
        destino = data.get("destino")

        if not origem or not destino:
            return jsonify({"erro": "Campos obrigatórios: origem, destino"}), 400

        lat1, lon1 = geocode_cached(origem)
        lat2, lon2 = geocode_cached(destino)

        if None in [lat1, lon1, lat2, lon2]:
            return jsonify({"erro": "Erro ao geocodificar os endereços"}), 400

        distancia, duracao = calcular_rota(lat1, lon1, lat2, lon2)
        if distancia is None:
            return jsonify({"erro": "Erro ao calcular rota"}), 500

        modelos = carregar_modelos()

        precos = {
            categoria: round(model.predict([[distancia, duracao]])[0], 2)
            for categoria, model in modelos.items()
        }

        tempo_exec = round(time.time() - start, 2)

        resposta = []
        for categoria, preco in precos.items():
            resposta.append({
                "DistanciaKM": round(distancia, 2),
                "TempoEstimado": f"{int(duracao // 60)}h {int(duracao % 60)}min",
                "TipoTransporte": categoria,
                "PrecoEstimado": preco
            })

        return jsonify({
            "origem": origem,
            "destino": destino,
            "resultados": resposta,
            "tempo_execucao_seg": tempo_exec
        })

    except Exception as e:
        return jsonify({"erro": str(e)}), 500


# === EXECUTAR ===
if __name__ == "__main__":
    app.run(debug=True, port=5000)