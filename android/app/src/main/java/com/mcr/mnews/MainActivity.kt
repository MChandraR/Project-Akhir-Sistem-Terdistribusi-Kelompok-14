package com.mcr.mnews

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mcr.mnews.fragment.AboutFragment
import com.mcr.mnews.util.ColorPalettes
import com.mcr.mnews.ui.theme.MNewsTheme

class MainActivity : ComponentActivity() {
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainMenu(this)

        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101);
        }
        val SP:SharedPreferences = getSharedPreferences("mcr",Context.MODE_PRIVATE)
        if(SP.getString("user_id","")!!!="") startActivity(Intent(this,MainMenu::class.java))
    }

    @Composable
    fun MainMenu(context:Context){
        MNewsTheme(color = ColorPalettes().BlueLight,context){
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = ColorPalettes().BlueLight
            ) {
                Column(modifier = Modifier.padding(horizontal =  50.dp),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(.5f),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text="Let's start \nyour \njourney",color = ColorPalettes().darkBlues, textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold, fontSize = 48.sp, lineHeight = 52.sp, fontFamily = FontFamily.SansSerif)
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(.3f), horizontalAlignment = Alignment.CenterHorizontally){
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 5.dp),colors =  ButtonDefaults.buttonColors(ColorPalettes().yellow),
                            onClick = {
                                context.startActivity(Intent(context,SignInMenu::class.java))
                            }
                        ){
                            Text(text="Sign in", fontSize=16.sp,color = ColorPalettes().darkBlues)
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 5.dp),
                            onClick = {
                                context.startActivity(Intent(context,SignUpMenu::class.java))
                            },
                            colors =  ButtonDefaults.buttonColors(Color.White)){
                            Text(text="Sign up", fontSize=16.sp,color = ColorPalettes().darkBlues)
                        }
                    }
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Akses penyimpanan diberikan !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Akses penyimpanan ditolak !", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}


