package com.example.mobile.api.model

data class RespostaAPI (
    var status : String,
    var totalResults : String,
    var articles: List<Noticia>
)