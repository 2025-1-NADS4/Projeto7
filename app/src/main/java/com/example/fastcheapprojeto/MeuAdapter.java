package com.example.fastcheapprojeto;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MeuAdapter extends RecyclerView.Adapter<MeuAdapter.ViewHolder> {

    private final List<String> lista;

    public MeuAdapter(List<String> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView texto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            texto = itemView.findViewById(R.id.textItem);
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
        holder.texto.setText(lista.get(position));
        String item = lista.get(position);
        holder.texto.setText(item);
        Log.d("MeuAdapter", "Item exibido: " + item);  // <-- ADICIONE AQUI


    }

    @Override
    public int getItemCount() {
        return lista.size();

    }
}


