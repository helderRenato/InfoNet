package com.example.mobile.api.model

import retrofit2.http.Url

data class Noticia (
    var source: Origem,
    var author : String,
    var title : String,
    var description : String,
    var url : String,
    var urlToImage : String,
    var publishedAt : String,
    var content : String
)