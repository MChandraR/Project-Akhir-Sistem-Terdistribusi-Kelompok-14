package com.mcr.mnews.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mcr.mnews.R
import com.mcr.mnews.ui.theme.MNewsTheme
import com.mcr.mnews.util.Pengembang
import com.mcr.mnews.util.util.colorPalettes

class AboutFragment {
    var pengembang:List<Pengembang> = listOf(
        Pengembang.Chandra,
        Pengembang.Irfan,
        Pengembang.Dimas,
        Pengembang.Ridho,
        Pengembang.Tiara,
        Pengembang.Rizsky
    )

    @Composable()
    fun aboutView(onClick:()->Unit={}){

        Column(
            Modifier
                .background(colorPalettes().darkBlues)
                .verticalScroll(rememberScrollState())){
            ConstraintLayout {
                val (innerText,background,btn)  =createRefs()

                Row(Modifier.constrainAs(background) {
                    top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start);end.linkTo(parent.end)
                }) {

                    Column (Modifier.weight(.6f)){
                        Image(
                            modifier = Modifier
                                .graphicsLayer { alpha = 0.99f }
                                .drawWithContent {
                                    val colors = listOf(
                                        Color.Black,
                                        Color.Transparent
                                    )
                                    drawContent()
                                    drawRoundRect(
                                        blendMode = BlendMode.DstOut,
                                        brush = Brush.horizontalGradient(
                                            colors = colors,
                                            endX = 200f
                                        ),
                                    )
                                    drawRoundRect(
                                        blendMode = BlendMode.DstAtop,
                                        brush = Brush.horizontalGradient(colors = colors),
                                    )
                                    drawRoundRect(
                                        blendMode = BlendMode.DstOut,
                                        brush = Brush.verticalGradient(colors = colors),
                                    )
                                }
                            ,
                            contentScale = ContentScale.Fit,
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = null
                        )
                    }
                    Column (Modifier.weight(.4f)){}
                }

                Column(
                    Modifier
                        .padding(start = 50.dp)
                        .constrainAs(innerText) {
                            top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start);end.linkTo(parent.end)
                        },
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(fontSize = 50.sp, fontWeight = FontWeight.Bold, text = "mNews App" , color = Color.Yellow)
                    Text(fontSize = 32.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold, text = "Aplikasi agregator berita" ,color = Color.Yellow)
                }

                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .constrainAs(btn) {
                            top.linkTo(parent.top); end.linkTo(parent.end, 10.dp)
                        },
                    onClick = onClick,
                    colors = IconButtonDefaults.iconButtonColors(Color.Transparent)
                ) {
                    Icon(modifier=Modifier.clip(CircleShape),imageVector = Icons.Default.Clear, contentDescription = null, tint = Color.White)
                }

            }

            Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(fontSize =20.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold, text = "Team pengembang :" ,color = Color.Yellow)
                var count = 0
                pengembang.forEach {
                    if(count%2==0) pengembangModelLeft(profile = it) else pengembangModelRight(
                        profile = it
                    )
                    count += 1
                }
                Button(modifier = Modifier.padding(top=20.dp),onClick = onClick, colors = ButtonDefaults.buttonColors(Color.Yellow), shape = RoundedCornerShape(50)) {
                    Text(text = "Close", color = colorPalettes().darkBlues)
                }
                Box(modifier = Modifier.height(100.dp))
            }
        }

    }

    @Composable
    fun pengembangModelLeft(profile:Pengembang){
        Box(modifier = Modifier
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(10))){
            Row(
                Modifier
                    .background(colorPalettes().white10)
                , verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier
                        .weight(.6f)
                        .padding(vertical = 10.dp)
                        .padding(start = 20.dp)) {
                    Text(fontSize = 20.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold, text = profile.nama ,color = Color.Yellow)
                    Text(fontSize = 20.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold, text = profile.nim ,color = Color.Yellow)
                    Text(fontSize = 16.sp, lineHeight = 20.sp,  text = profile.desc ,color = Color.Yellow)

                }
                Column(
                    Modifier
                        .weight(.4f)
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .padding(end = 20.dp), horizontalAlignment = Alignment.End) {
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(100.dp),painter = painterResource(id = profile.foto), contentDescription = null)
                }
            }
        }
    }
    @Composable
    fun pengembangModelRight(profile:Pengembang){
        Box(modifier = Modifier
            .padding(vertical = 20.dp)
            .clip(RoundedCornerShape(10))) {
            Row(
                Modifier
                    .background(colorPalettes().white10)
                , verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier
                        .weight(.4f)
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .padding(start = 20.dp), horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(100.dp),
                        painter = painterResource(id = profile.foto),
                        contentDescription = null
                    )
                }
                Column(
                    Modifier
                        .weight(.6f)
                        .padding(vertical = 10.dp)
                        .padding(end = 20.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        text = profile.nama,
                        color = Color.Yellow,
                        textAlign = TextAlign.Right
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        text = profile.nim,
                        color = Color.Yellow,
                        textAlign = TextAlign.Right
                    )
                    Text(modifier = Modifier.fillMaxWidth(),fontSize = 16.sp, lineHeight = 20.sp,  text = profile.desc ,color = Color.Yellow, textAlign = TextAlign.Right)

                }

            }
        }
    }
}