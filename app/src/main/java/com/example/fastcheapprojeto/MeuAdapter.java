package com.example.fastcheapprojeto;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MeuAdapter extends RecyclerView.Adapter<MeuAdapter.ViewHolder> {
    private List<Transportes> listaTransportes;
    private double menorPreco;

    public MeuAdapter(List<Transportes> listaTransportes) {
        this.listaTransportes = listaTransportes;
        if (listaTransportes != null && !listaTransportes.isEmpty()) {
            this.menorPreco = listaTransportes.get(0).getPreco();  // lista já ordenada, menor preco é o primeiro
        } else {
            this.menorPreco = 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTipoTransporte;
        ImageView logoTransporte;
        TextView preco;

        public ViewHolder(View itemView) {
            super(itemView);
            textTipoTransporte = itemView.findViewById(R.id.textTipoTransporte);
            logoTransporte = itemView.findViewById(R.id.logoTransporte);
            preco = itemView.findViewById(R.id.textPreco);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transportes transporte = listaTransportes.get(position);
        holder.textTipoTransporte.setText(transporte.getNome());
        holder.preco.setText("R$ " + transporte.getPreco());
        holder.logoTransporte.setImageResource(transporte.getImagemResId());

        double preco = transporte.getPreco();

        if (preco == menorPreco) {
            holder.itemView.setBackgroundColor(Color.parseColor("#41B374")); // Verde claro
        } else if (preco <= menorPreco * 1.1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#77C768")); // Verde médio
        } else if (preco <= menorPreco * 1.2) {
            holder.itemView.setBackgroundColor(Color.parseColor("#B4D94D")); // Verde escuro
        } else if (preco <= menorPreco * 1.3) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFD700")); // Amarelo
        } else if (preco <= menorPreco * 1.5) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FF8C00")); // Laranja
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#B22222")); // Vermelho
        }
    }

    @Override
    public int getItemCount() {
        return listaTransportes != null ? listaTransportes.size() : 0;
    }
}
