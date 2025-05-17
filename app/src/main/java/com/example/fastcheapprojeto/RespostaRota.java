package com.example.fastcheapprojeto;

import android.os.Parcel;
import android.os.Parcelable;

public class RespostaRota implements Parcelable {
    private String rota;
    private double distanciaKM;
    private String tempoEstimado;
    private String tipoTransporte;
    private double precoEstimado;

    public String getRota() { return rota; }
    public double getDistanciaKM() { return distanciaKM; }
    public String getTempoEstimado() { return tempoEstimado; }
    public String getTipoTransporte() { return tipoTransporte; }
    public double getPrecoEstimado() { return precoEstimado; }

    // Construtor padrão (opcional)
    public RespostaRota(String rota, double distanciaKM, String tempoEstimado, String tipoTransporte, double precoEstimado) {
        this.rota = rota;
        this.distanciaKM = distanciaKM;
        this.tempoEstimado = tempoEstimado;
        this.tipoTransporte = tipoTransporte;
        this.precoEstimado = precoEstimado;
    }

    // Parcelable
    protected RespostaRota(Parcel in) {
        rota = in.readString();
        distanciaKM = in.readDouble();
        tempoEstimado = in.readString();
        tipoTransporte = in.readString();
        precoEstimado = in.readDouble();
    }

    public static final Creator<RespostaRota> CREATOR = new Creator<RespostaRota>() {
        @Override
        public RespostaRota createFromParcel(Parcel in) {
            return new RespostaRota(in);
        }

        @Override
        public RespostaRota[] newArray(int size) {
            return new RespostaRota[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(rota);
        parcel.writeDouble(distanciaKM);
        parcel.writeString(tempoEstimado);
        parcel.writeString(tipoTransporte);
        parcel.writeDouble(precoEstimado);
    }
}
