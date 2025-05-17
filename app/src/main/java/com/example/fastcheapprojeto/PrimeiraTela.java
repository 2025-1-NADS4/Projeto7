package com.example.fastcheapprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

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
        String locInicio = localInicio.getText().toString();
        String locFinal = localFinal.getText().toString();

        if (locInicio.isEmpty() || locFinal.isEmpty()) {
            Log.d("API", "Por favor, preencha os dois campos.");
            return;
        }

        Intent intentLoading = new Intent(this, telaloading.class);
        startActivity(intentLoading);

        InputRota inputRota = new InputRota(locInicio, locFinal);

        Api api = RetrofitClient.getRetrofitInstance().create(Api.class);
        Call<RespostaRota> call = api.obterRota(inputRota);

        call.enqueue(new Callback<RespostaRota>() {
            @Override
            public void onResponse(Call<RespostaRota> call, Response<RespostaRota> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RespostaRota rota = response.body();
                    Log.d("API", "Sucesso! Dados recebidos:");
                    Log.d("API", "Rota: " + rota.getRota());
                    Log.d("API", "Distância (KM): " + rota.getDistanciaKM());
                    Log.d("API", "Tempo Estimado: " + rota.getTempoEstimado());
                    Log.d("API", "Tipo Transporte: " + rota.getTipoTransporte());
                    Log.d("API", "Preço Estimado: " + rota.getPrecoEstimado());

                    // Espera 5 segundos antes de iniciar a SegundaTela e fechar loading
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(PrimeiraTela.this, SegundaTela.class);
                        intent.putExtra("Rota", rota.getRota());
                        intent.putExtra("DistanciaKM", rota.getDistanciaKM());
                        intent.putExtra("TempoEstimado", rota.getTempoEstimado());
                        intent.putExtra("TipoTransporte", rota.getTipoTransporte());
                        intent.putExtra("PrecoEstimado", rota.getPrecoEstimado());
                        intent.putExtra("LocalPartida", locInicio);
                        intent.putExtra("LocalFinal", locFinal);
                        startActivity(intent);

                        finish();
                    }, 1000);

                } else {
                    Log.e("API", "Resposta falhou: " + response.code());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RespostaRota> call, Throwable t) {
                Log.e("API", "Falha na requisição: " + t.getMessage());
                finish();
            }
        });
    }
}
