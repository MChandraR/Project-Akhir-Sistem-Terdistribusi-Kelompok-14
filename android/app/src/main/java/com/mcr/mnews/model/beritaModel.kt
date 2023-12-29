package com.mcr.mnews.model

import com.google.gson.annotations.SerializedName

class beritaModel(bookmarked:Boolean=false) {
    @SerializedName("berita_id")
    var berita_id:String = ""


    @SerializedName("mark_id")
    var mark_id:String = ""

    @SerializedName("gambar")
    var gambar:String = ""

    @SerializedName("deskripsi")
    var deskripsi:String = "JAKARTA, Dec 4 (Reuters) - Eleven climbers were found dead in Indonesia on Monday and 12 were missing after the Marapi volcano erupted in West Sumatra, a rescue official said, as search operations - which were halted temporarily over safety concerns - resumed.\n" +
            "\n" +
            "Three survivors were found on Monday\n\nBanyak Orang yang meninggal atau mati akibat erupsi Marapi"

    @SerializedName("author")
    var author:String = "Chandra.R"

    @SerializedName("url")
    var url:String = ""

    @SerializedName("waktu")
    var waktu:String = "2023-12-04 08:16:00"

    @SerializedName("judul")
    var judul:String = "Judul Berita Sementara"

    @SerializedName("sumber")
    var sumber:String = "CNN NEws"

    @SerializedName("kategori")
    var kategori:String = "terbaru"

    @SerializedName("bookmarked")
    var bookmarked:Boolean = bookmarked

    class searchModel(key:String){
        @SerializedName("keyword")
        var keyword:String = key
    }
}