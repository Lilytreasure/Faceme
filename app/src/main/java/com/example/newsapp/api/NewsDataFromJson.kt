package com.example.newsapp.api

data class NewsDataFromJson(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)