package com.mcr.mnews.interfaces

import com.mcr.mnews.model.notifModel
import retrofit2.Call
import retrofit2.http.GET

interface notifAPI {
    @GET("api/notif")
    fun getNotification(): Call<ArrayList<notifModel>>
}