package com.example.electronic_journal.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Синглтон для установки подключения с удалённым сервером
 */
object WebServerSingleton {

    private const val WEB_SRV_URL = "http://10.0.2.2:8080/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WEB_SRV_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}