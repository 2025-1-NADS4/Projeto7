package com.example.fastcheapprojeto;

import com.example.fastcheapprojeto.InputRota;
import com.example.fastcheapprojeto.RespostaRota;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
    @POST("api/viagem/calcular-rota")
    Call<List<RespostaRota>> obterRota(@Body InputRota input);  // <-- Aqui deve ser List<RespostaRota>
}
