package com.baloot.mirzazade.connection

import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

object ApiClient  {

    val BASE_URL = "https://newsapi.org/v2/"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}