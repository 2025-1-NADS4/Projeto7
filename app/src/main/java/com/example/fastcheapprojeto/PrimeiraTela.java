package com.example.fastcheapprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class PrimeiraTela extends AppCompatActivity {
    //Aqui eu declaro as variaveis.
    private TextInputEditText localInicio;
    private TextInputEditText localFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_telaloading); //Alterar para activity_primeira_tela

        //Usa a variavel declarada no inicio, depois busca o id do XML.
        localInicio = findViewById(R.id.textInputPartida);
        localFinal = findViewById(R.id.textInputFinal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void btnTelaDois(View view) {
        //Preciso pegar os dados do editText, no caso esses acima, apenas altero o nome dessa variavel, mas uso o valor que foi definido acima pelo ID.
        String locInicio = localInicio.getText().toString();
        String locFinal = localFinal.getText().toString();

        // Cria um Intent para iniciar a SegundaTela
        Intent intent = new Intent(this, SegundaTela.class);
        intent.putExtra("LocalPartida", locInicio);
        intent.putExtra("LocalFinal", locFinal);
        // Inicia a nova Activity
        startActivity(intent);
    }
}