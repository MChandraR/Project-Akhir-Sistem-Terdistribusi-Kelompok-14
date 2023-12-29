package com.mcr.mnews.model

import com.google.gson.annotations.SerializedName

class UserModel {
    class ProfileModel{
        @SerializedName("profile_url")
        var profile_url:String = ""

        @SerializedName("key")
        var key:String = ""

    }
}