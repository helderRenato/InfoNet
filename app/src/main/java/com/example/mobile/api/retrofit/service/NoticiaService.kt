package com.example.peoplefinder.API.retrofit.service

import com.example.mobile.api.model.RespostaAPI
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface NoticiaService{
    @GET("top-headlines")
    fun getNoticias(
        @Query("q") q : String,
        @Query("apiKey") apiKey : String,
        @Query("country") country : String,
        @Query("category") category : String
    ): Call<RespostaAPI>
}