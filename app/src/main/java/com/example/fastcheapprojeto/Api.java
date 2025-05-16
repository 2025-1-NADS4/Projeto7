package com.example.fastcheapprojeto;

import com.example.fastcheapprojeto.InputRota;
import com.example.fastcheapprojeto.RespostaRota;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;




public interface Api {
    @POST("api/viagem/calcular-rota") // Aqui eu defino a rota da api
    Call<RespostaRota> obterRota(@Body InputRota input);
}
