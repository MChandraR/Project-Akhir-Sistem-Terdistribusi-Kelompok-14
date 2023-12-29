package com.mcr.mnews.fragment

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcr.mnews.interfaces.notifAPI
import com.mcr.mnews.model.notifModel
import com.mcr.mnews.util.clientAPI
import com.mcr.mnews.util.util.colorPalettes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotifFragment(val context:Context) {
    var notifList:ArrayList<notifModel> = ArrayList()
    var dataCount: MutableState<Int> = mutableStateOf(0)
    var showCount: MutableState<Int> = mutableStateOf(0)
    var shows:MutableState<ArrayList<Boolean>> = mutableStateOf(ArrayList())

    init {
        fetchNotifData()
    }

    @Composable
    fun notifMainView(){
        var color  = MaterialTheme.colorScheme.inverseSurface

        Column(
            Modifier
                .padding(10.dp)
                .padding(top = 10.dp)
                .fillMaxSize()
            ){
            LazyColumn(content ={
                items(
                    count = dataCount.value,
                    itemContent = {
                        shows.value.add(true)
                        notifModelView(data = notifList[it],it)
                    }
                )
            })
        }
    }

    fun fetchNotifData(){
        val api = clientAPI().getClientAPI()
        val notifAPI = api.create(notifAPI::class.java)
        val resData = notifAPI.getNotification()

        resData.enqueue(object:Callback<ArrayList<notifModel>>{
            override fun onResponse(
                call: Call<ArrayList<notifModel>>,
                response: Response<ArrayList<notifModel>>
            ) {
                if(response.body()!=null){
                    notifList.clear()
                    notifList.addAll(response.body()!!)
                    dataCount.value = notifList.size
                }
            }

            override fun onFailure(call: Call<ArrayList<notifModel>>, t: Throwable) {
               Toast.makeText(context, "Tidak dapat terhubung ke server !",Toast.LENGTH_SHORT).show()
            }
        })
    }

    @Composable
    fun notifModelView(data:notifModel,idx:Int){
        var show by remember {
            mutableStateOf(0)
        }
        show = showCount.value
        AnimatedVisibility(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .clip(
                    RoundedCornerShape(10)
                ),
            visible = shows.value[idx],
            enter = slideInHorizontally{it},
            exit = slideOutHorizontally { it }
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(.1f)) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.outline )
                }
                Column (Modifier.weight(.8f)){
                    Text(text=data.message, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                }
                Column(Modifier.weight(.1f), horizontalAlignment = Alignment.End) {
                    Icon(modifier = Modifier.clickable(true, onClick = {
                        shows.value[idx] = false
                        showCount.value += 1
                    }), imageVector = Icons.Default.Clear, contentDescription = null, tint = MaterialTheme.colorScheme.outline )
                }
            }
        }
    }

}