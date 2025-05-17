package com.example.fastcheapprojeto;

public class Transportes {
    String nome;
    int imagemResId;
    double preco;

    public Transportes(String nome, int imagemResId, double preco) {
        this.nome = nome;
        this.imagemResId = imagemResId;
        this.preco = preco;
    }

    public String getNome() { return nome; }
    public int getImagemResId() { return imagemResId; }
    public double getPreco() { return preco; }
}
