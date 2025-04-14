package com.example.fastcheapprojeto;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.util.Log;


public class SegundaTela extends AppCompatActivity {
    private TextView textPartidaDados;
    private TextView textFinalDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_segunda_tela);

        textPartidaDados = findViewById(R.id.textPartida);
        textFinalDados = findViewById(R.id.textFinal);

        String localPartida = getIntent().getStringExtra("LocalPartida");
        String localFinal = getIntent().getStringExtra("LocalFinal");

        textPartidaDados.setText(localPartida);
        textFinalDados.setText(localFinal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Transportes> listaTransportes = new ArrayList<>();
        listaTransportes.add(new Transportes("Uber", R.drawable.logouber, 20.00, 30.90));
        listaTransportes.add(new Transportes("UberX", R.drawable.logouber, 40.0, 50.90));
        listaTransportes.add(new Transportes("99 Pop", R.drawable.logo99, 75.0, 85.90));
        listaTransportes.add(new Transportes("99 Plus", R.drawable.logo99, 95.0, 100.90));
        listaTransportes.add(new Transportes("Taxi", R.drawable.taxi, 105.50, 130.90));//Adicionar aqui se quiser mais itens


        //teste do sort
        Collections.sort(listaTransportes, new Comparator<Transportes>() {
            @Override
            public int compare(Transportes t1, Transportes t2) {
                return Double.compare(t1.precoMin, t2.precoMax);
            }
        });

        //end teste sort

        MeuAdapter adapter = new MeuAdapter(listaTransportes);
        recyclerView.setAdapter(adapter);



    }
}