package com.example.fastcheapprojeto;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SegundaTela extends AppCompatActivity {
    private TextView textPartidaDados;
    private TextView textFinalDados;
    private TextView textDistancia;
    private TextView textTempoEstimado;
    private TextView textTipoTransporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_tela);

        textPartidaDados = findViewById(R.id.textPartida);
        textFinalDados = findViewById(R.id.textFinal);
        textDistancia = findViewById(R.id.textDistancia);
        textTempoEstimado = findViewById(R.id.textTempoEstimado);
        textTipoTransporte = findViewById(R.id.textTipoTransporte);

        String localPartida = getIntent().getStringExtra("LocalPartida");
        String localFinal = getIntent().getStringExtra("LocalFinal");
        double distancia = getIntent().getDoubleExtra("DistanciaKM", -1);
        String tempoEstimado = getIntent().getStringExtra("TempoEstimado");
        String tipoTransporte = getIntent().getStringExtra("TipoTransporte");
        double preco = getIntent().getDoubleExtra("PrecoEstimado", -1);

        textPartidaDados.setText(localPartida != null ? localPartida : "");
        textFinalDados.setText(localFinal != null ? localFinal : "");
        textDistancia.setText(distancia >= 0 ? distancia + " km" : "-- km");
        textTempoEstimado.setText(tempoEstimado != null ? tempoEstimado : "--");
        textTipoTransporte.setText(tipoTransporte != null ? "Tipo Transporte: " + tipoTransporte : "Tipo Transporte: --");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtem a imagem certa de acordo com o tipo de transporte
        int imagem = getImagemPorTipo(tipoTransporte);

        List<Transportes> listaTransportes = new ArrayList<>();
        listaTransportes.add(new Transportes(
                tipoTransporte != null ? tipoTransporte : "Tipo desconhecido",
                imagem,
                preco
        ));

        // Exemplo de ordenação pelo preço, se quiser ordenar uma lista maior
        Collections.sort(listaTransportes, Comparator.comparingDouble(Transportes::getPreco));

        MeuAdapter adapter = new MeuAdapter(listaTransportes);
        recyclerView.setAdapter(adapter);
    }

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
