package com.example.fastcheapprojeto;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MeuAdapter extends RecyclerView.Adapter<MeuAdapter.ViewHolder> {

    private final List<Transportes> lista;

    public MeuAdapter(List<Transportes> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView texto;
        TextView preco;
        ImageView imagem;
        ConstraintLayout itemBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            texto = itemView.findViewById(R.id.textItem);
            preco = itemView.findViewById(R.id.textPreco);
            imagem = itemView.findViewById(R.id.logo);
            itemBackground = itemView.findViewById(R.id.itemBackground);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transportes item = lista.get(position);
        holder.texto.setText(item.getNome());
        holder.preco.setText(String.format("%.2f - %.2f R$", item.precoMin, item.precoMax));
        holder.imagem.setImageResource(item.getImagemResId());

        //Definir cor
        double precoMin = item.getPrecoMin();

        if (precoMin <= 25) {
            holder.itemBackground.setBackgroundColor(Color.parseColor("#2ecc71")); // Verde
        } else if (precoMin <= 50) {
            holder.itemBackground.setBackgroundColor(Color.parseColor("#f1c40f")); // Amarelo
        } else if (precoMin <= 100) {
            holder.itemBackground.setBackgroundColor(Color.parseColor("#e67e22")); // Laranja
        } else {
            holder.itemBackground.setBackgroundColor(Color.parseColor("#e74c3c")); // Vermelho
        }
        //final teste



        Log.d("MeuAdapter", "Item exibido: " + item.getNome());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}



