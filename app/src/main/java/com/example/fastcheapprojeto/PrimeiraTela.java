package com.example.fastcheapprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
        setContentView(R.layout.activity_primeira_tela); //Alterar para activity_primeira_tela

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

        //Inicia a tela de loading
        Intent intentLoading = new Intent(this, telaloading.class);
        startActivity(intentLoading);

        //Aqui eu uso o handle para após aguardar o tempo de X segundos ele iniciar a minha terceira tela
        new Handler().postDelayed(() -> {
            // Cria um Intent para iniciar a SegundaTela e pegar os dados da primeira tela e enviar para a terceira
            Intent intent = new Intent(this, SegundaTela.class);
            intent.putExtra("LocalPartida", locInicio);
            intent.putExtra("LocalFinal", locFinal);
            // Inicia a Activity segundatela
            startActivity(intent);

            //Fecha a tela de loading sozinha após atingir o tempo escolhido
            finish();

        }, 9000);//Definir o tempo.

    }
}