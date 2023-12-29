package com.mcr.mnews.model

import com.google.gson.annotations.SerializedName

class notifModel {
    @SerializedName("notifID")
    var notifID :String = ""

    @SerializedName("message")
    var message:String = ""
}