package com.mcr.mnews.model

import com.google.gson.annotations.SerializedName

class LoginModel() {
    @SerializedName("status")
    var status:String = ""

    @SerializedName("message")
    var message:String = ""

    @SerializedName("data")
    var data: loginData = loginData()

    class loginData{
        @SerializedName("username")
        var username:String = ""

        @SerializedName("password")
        var password:String = ""

        @SerializedName("user_id")
        var user_id:String = ""

        @SerializedName("email")
        var email:String = ""

        @SerializedName("role")
        var role:String = ""

    }

    class updateModel(uid:String,usrnm:String, pass:String, mail:String, updatePas:Int){
        @SerializedName("status")
        var status:String = ""

        @SerializedName("message")
        var message:String = ""

        @SerializedName("data")
        var data: loginData = loginData()

        @SerializedName("username")
        var username:String = usrnm

        @SerializedName("password")
        var password:String = pass

        @SerializedName("user_id")
        var user_id:String = uid

        @SerializedName("email")
        var email:String = mail

        @SerializedName("updatePass")
        var updatePass:Int = updatePas

    }

    class imageUploadModel(){
        @SerializedName("status")
        var status:String = ""

        @SerializedName("message")
        var message:String = ""
    }
}