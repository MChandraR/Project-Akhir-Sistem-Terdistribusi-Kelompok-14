package com.mcr.mnews.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class bookMarkModel(beritaModel: beritaModel, user_id:String) {
    @SerializedName("berita_id")
    var berita_id = beritaModel.berita_id

    @SerializedName("judul")
    var judul = beritaModel.judul

    @SerializedName("gambar")
    var gambar = beritaModel.gambar

    @SerializedName("deskripsi")
    var deskripsi = beritaModel.deskripsi

    @SerializedName("author")
    var author = beritaModel.author

    @SerializedName("url")
    var url = beritaModel.url

    @SerializedName("waktu")
    var waktu = beritaModel.waktu

    @SerializedName("sumber")
    var sumber = beritaModel.sumber

    @SerializedName("user_id")
    var user_id = user_id

    @SerializedName("status")
    var status = ""

    @SerializedName("message")
    var message = ""

    @SerializedName("kategori")
    var kategori:String = beritaModel.kategori

    @SerializedName("data")
    var data:ArrayList<beritaModel> = ArrayList()

    class removeModel(markID:String){
        @SerializedName("mark_id")
        var mark_id = markID

        @SerializedName("status")
        var status = ""

        @SerializedName("message")
        var message = ""
    }

    class searchModel(id:String,key:String){
        @SerializedName("user_id")
        var user_id = id

        @SerializedName("keyword")
        var keyword = key
    }

    class bookmarkResult{
        @SerializedName("status")
        var status = ""

        @SerializedName("message")
        var message = ""

        @SerializedName("data")
        var data:ArrayList<beritaModel> = ArrayList()
    }

}