package com.example.fastcheapprojeto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MeuAdapter extends RecyclerView.Adapter<MeuAdapter.ViewHolder> {
    private List<Transportes> listaTransportes;

    // Construtor OK
    public MeuAdapter(List<Transportes> listaTransportes) {
        this.listaTransportes = listaTransportes;
    }

    // ViewHolder OK
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

    // onCreateViewHolder OK
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new ViewHolder(view);
    }

    // onBindViewHolder corrigido
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transportes transporte = listaTransportes.get(position);

        // Define o texto do tipo de transporte
        holder.textTipoTransporte.setText(
                transporte.getNome() != null ? transporte.getNome() : "--"
        );

        // Define o preço formatado
        holder.preco.setText("R$ " + String.format("%.2f", transporte.getPreco()));

        // Atribui a imagem ao ImageView (aqui a correção)
        holder.logoTransporte.setImageResource(transporte.getImagemResId());
    }

    // getItemCount OK
    @Override
    public int getItemCount() {
        return listaTransportes != null ? listaTransportes.size() : 0;
    }
}
