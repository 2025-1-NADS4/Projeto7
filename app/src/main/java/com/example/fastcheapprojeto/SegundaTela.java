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

import java.util.Arrays;
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

        // Lista de exemplo
        List<String> dados = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4");//Aqui eu coloco a quantidade que eu quero que aparece na tela.

        // Adapter
        MeuAdapter adapter = new MeuAdapter(dados);

        // Layout (vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



    }
}