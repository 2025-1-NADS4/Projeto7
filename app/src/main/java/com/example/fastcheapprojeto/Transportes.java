package com.example.fastcheapprojeto;

public class Transportes {
    String nome;
    int imagemResId;
    double precoMin;
    double precoMax;

    public Transportes(String nome, int imagemResId, double precoMin, double precoMax) {
        this.nome = nome;
        this.imagemResId = imagemResId;
        this.precoMin = precoMin;
        this.precoMax = precoMax;
    }

    public String getNome() { return nome; }
    public int getImagemResId() { return imagemResId; }
    public double getPrecoMin() { return precoMin; }
    public double getPrecoMax() {return precoMax; }
}

