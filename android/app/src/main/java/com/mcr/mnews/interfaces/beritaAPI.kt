package com.mcr.mnews.interfaces

import com.mcr.mnews.model.beritaModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface beritaAPI {
    @GET("api/berita")
    fun getDataBerita():Call<ArrayList<beritaModel>>

    @GET()
    fun getBeritabyCategory(@Url url:String):Call<ArrayList<beritaModel>>

    @POST("api/berita")
    fun searchBerta(@Body body:beritaModel.searchModel):Call<java.util.ArrayList<beritaModel>>
}