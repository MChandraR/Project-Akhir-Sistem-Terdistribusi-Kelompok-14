package com.mcr.mnews.interfaces

import com.mcr.mnews.model.bookMarkModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.ArrayList

interface bookMarkAPI {
    @POST("api/bookmarks")
    fun getUserBookmark(@Body body: bookMarkModel) : Call<bookMarkModel>

    @POST("api/bookmark")
    fun addBookMark(@Body body: bookMarkModel): Call<bookMarkModel>

    @POST("api/bookmark/search")
    fun searchBookmark(@Body body: bookMarkModel.searchModel): Call<bookMarkModel.bookmarkResult>

    @PUT("api/bookmark")
    fun removeBookmark(@Body body: bookMarkModel.removeModel):Call<bookMarkModel.removeModel>
}