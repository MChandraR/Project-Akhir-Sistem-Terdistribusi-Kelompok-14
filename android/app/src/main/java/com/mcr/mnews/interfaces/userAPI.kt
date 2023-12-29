package com.mcr.mnews.interfaces

import com.mcr.mnews.model.LoginModel
import com.mcr.mnews.model.UserModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Url

interface userAPI {
    @POST("api/login")
    fun signIn(@Body body: LoginModel.loginData ):Call<LoginModel>

    @POST("api/signup")
    fun signUp(@Body body: LoginModel.loginData) : Call<LoginModel>

    @PUT("api/profile")
    fun updateProfile(@Body body:LoginModel.updateModel):Call<LoginModel.updateModel>

    @GET()
    fun getProfileUrl(@Url url:String):Call<UserModel.ProfileModel>

    @Multipart
    @POST("api/upload/profile")
    fun updateUserImage(@Part image:MultipartBody.Part):Call<LoginModel.imageUploadModel>
}