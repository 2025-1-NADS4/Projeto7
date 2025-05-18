package com.example.fastcheapprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrimeiraTela extends AppCompatActivity {
    private TextInputEditText localInicio;
    private TextInputEditText localFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_primeira_tela);

        localInicio = findViewById(R.id.textInputPartida);
        localFinal = findViewById(R.id.textInputFinal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void btnTelaDois(View view) {
        String locInicio = localInicio.getText().toString().trim();
        String locFinal = localFinal.getText().toString().trim();

        if (locInicio.isEmpty() || locFinal.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha os dois campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intentLoading = new Intent(this, telaloading.class);
        startActivity(intentLoading);

        InputRota inputRota = new InputRota(locInicio, locFinal);

        Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
        Call<List<RespostaRota>> call = api.obterRota(inputRota);

        call.enqueue(new Callback<List<RespostaRota>>() {
            @Override
            public void onResponse(Call<List<RespostaRota>> call, Response<List<RespostaRota>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<RespostaRota> rotas = response.body();

                    Log.d("API", "Sucesso! Número de rotas recebidas: " + rotas.size());

                    for (RespostaRota rota : rotas) {
                        Log.d("API", "Rota: " + rota.getRota());
                        Log.d("API", "Distância (KM): " + rota.getDistanciaKM());
                        Log.d("API", "Tempo Estimado: " + rota.getTempoEstimado());
                        Log.d("API", "Tipo Transporte: " + rota.getTipoTransporte());
                        Log.d("API", "Preço Estimado: " + rota.getPrecoEstimado());
                    }

                    Gson gson = new Gson();
                    String rotasJson = gson.toJson(rotas);

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(PrimeiraTela.this, SegundaTela.class);
                        intent.putExtra("rotasJson", rotasJson);
                        intent.putExtra("LocalPartida", locInicio);
                        intent.putExtra("LocalFinal", locFinal);
                        startActivity(intent);

                        finish();
                    }, 5000);

                } else {
                    Log.e("API", "Resposta falhou ou lista vazia: " + response.code());
                    Toast.makeText(PrimeiraTela.this, "Falha ao receber dados da rota.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RespostaRota>> call, Throwable t) {
                Log.e("API", "Falha na requisição: " + t.getMessage());
                Toast.makeText(PrimeiraTela.this, "Erro na conexão. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
