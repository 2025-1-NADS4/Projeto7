package com.example.fastcheapprojeto;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SegundaTela extends AppCompatActivity {

    private TextView textPartidaDados;
    private TextView textFinalDados;
    private TextView textDistancia;
    private TextView textTipoTransporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_tela);

        // Recebe JSON das rotas via Intent e converte para lista de objetos
        String rotasJson = getIntent().getStringExtra("rotasJson");
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<RespostaRota>>(){}.getType();
        List<RespostaRota> rotas = gson.fromJson(rotasJson, tipoLista);

        // Inicializa os TextViews
        textPartidaDados = findViewById(R.id.textPartida);
        textFinalDados = findViewById(R.id.textFinal);
        textDistancia = findViewById(R.id.textDistancia);
        textTipoTransporte = findViewById(R.id.textTipoTransporte);
        // textTempoEstimado removido

        // Recebe strings extras via Intent
        String localPartida = getIntent().getStringExtra("LocalPartida");
        String localFinal = getIntent().getStringExtra("LocalFinal");

        // Define textos das partidas e destino
        textPartidaDados.setText(localPartida != null ? localPartida : "");
        textFinalDados.setText(localFinal != null ? localFinal : "");

        // Cria lista para exibir transportes com imagem e preço
        List<Transportes> listaTransportes = new ArrayList<>();
        if (rotas != null && !rotas.isEmpty()) {
            for (RespostaRota rota : rotas) {
                int imagemAtual = getImagemPorTipo(rota.getTipoTransporte());
                listaTransportes.add(new Transportes(
                        rota.getTipoTransporte(),
                        imagemAtual,
                        rota.getPrecoEstimado()
                ));
            }
        }

        // Ordena lista pelo preço para encontrar menor preço
        Collections.sort(listaTransportes, Comparator.comparingDouble(Transportes::getPreco));
        double menorPreco = listaTransportes.isEmpty() ? 0 : listaTransportes.get(0).getPreco();

        // Busca rota mais barata para preencher distância e tempo
        RespostaRota rotaMaisBarata = null;
        if (rotas != null) {
            for (RespostaRota rota : rotas) {
                if (rota.getPrecoEstimado() == menorPreco) {
                    rotaMaisBarata = rota;
                    break;
                }
            }
        }

        if (rotaMaisBarata != null) {
            String texto = String.format("Duração: %s | %.1fkm", rotaMaisBarata.getTempoEstimado(), rotaMaisBarata.getDistanciaKM());
            textDistancia.setText(texto);
            textTipoTransporte.setText("Tipo Transporte: " + rotaMaisBarata.getTipoTransporte());
        } else {
            textDistancia.setText("-- | --");
            textTipoTransporte.setText("Tipo Transporte: --");
        }

        // Ajusta padding para as insets (barras do sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configura RecyclerView para mostrar lista de transportes
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MeuAdapter adapter = new MeuAdapter(listaTransportes);

        recyclerView.setAdapter(adapter);
    }

    // Retorna drawable correto de acordo com tipo de transporte
    private int getImagemPorTipo(String tipoTransporte) {
        if (tipoTransporte == null) return R.drawable.logo;

        switch (tipoTransporte.toLowerCase()) {
            case "uber":
                return R.drawable.logouber;
            case "99":
                return R.drawable.logo99;
            case "taxi":
                return R.drawable.taxi;
            default:
                return R.drawable.logo;
        }
    }
}
