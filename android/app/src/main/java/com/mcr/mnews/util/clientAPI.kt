package com.mcr.mnews.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class clientAPI {
    var serverURL = "https://m-news-server.vercel.app/"

    fun getClientAPI():Retrofit{
        return Retrofit.Builder().baseUrl(serverURL).addConverterFactory(GsonConverterFactory.create()).build()
    }
}