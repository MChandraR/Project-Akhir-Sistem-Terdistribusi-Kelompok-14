package com.mcr.mnews.util

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mcr.mnews.R
import com.mcr.mnews.util.util.colorPalettes

class Dialog {
    val dialogSuccess : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_check_circle_outline_24,
        Color.Green,
        "Login Berhasil",
        "Selamat datang !",
        mod = Modifier.padding(horizontal = 50.dp)
    )
    val dialogProfileSuccess : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_check_circle_outline_24,
        Color.Green,
        "Berhasil",
        "Berhasil mengupdate data profile !",
        mod = Modifier.padding(horizontal = 50.dp)
    )
    val dialogProfileImageSuccess : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_check_circle_outline_24,
        Color.Green,
        "Berhasil",
        "Berhasil mengupdate foro profile !",
        mod = Modifier.padding(horizontal = 50.dp)
    )
    val dialogRegistSuccess : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_check_circle_outline_24,
        Color.Green,
        "Berhasil menambahkan user baru !",
        "Selamat datang ^-^, silahkan login pada halaman signIn.!",
        mod = Modifier.padding(horizontal = 50.dp)
    )

    val dialogThemeSuccess : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_check_circle_outline_24,
        Color.Green,
        "Berhasil merubah pengaturan !",
        "Silahkan mulai ulang aplikasi untuk melihat perubahan !",
        mod = Modifier.padding(horizontal = 50.dp)
    )
    val dialogError : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_do_disturb_96,
        Color.Red,
        "Login Gagal !",
        "Username atau password salah !",
        mod = Modifier.padding(horizontal = 50.dp)
    )
    val dialogProfileError : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_check_circle_outline_24,
        Color.Green,
        "Gagal",
        "Gagal mengupdate profile, coba lagi nanti !",
        mod = Modifier.padding(horizontal = 50.dp)
    )
    val dialogUpdateUserError : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_do_disturb_96,
        Color.Red,
        "Gagal mengupdate data user !",
        "Coba ulang kembali atau pastikan data benar !",
        mod = Modifier.padding(horizontal = 50.dp)
    )
    val dialogRegistError : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_do_disturb_96,
        Color.Red,
        "Gagal menambahkan data user !",
        "Periksa kembali data anda, pastikan username dan email unik !",
        mod = Modifier.padding(horizontal = 50.dp)
    )
    val dialogRegistReject : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_outlet_96,
        colorPalettes().BlueLight,
        "Data Invalid !",
        "Periksa kembali data anda, data tidak boleh kosong!",
        mod = Modifier.padding(horizontal = 50.dp)
    )
    val dialogRto : myCustomUI.DialogModel = myCustomUI.DialogModel(
        Color.White,
        R.drawable.baseline_wifi_off_96,
        colorPalettes().BlueLight,
        "Request Time Out",
        "Tidak dapat terhubung ke server !",
        mod = Modifier.padding(horizontal = 50.dp)
    )

}