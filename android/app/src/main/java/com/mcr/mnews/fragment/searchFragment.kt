package com.mcr.mnews.fragment

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mcr.mnews.interfaces.beritaAPI
import com.mcr.mnews.model.beritaModel
import com.mcr.mnews.util.clientAPI
import com.mcr.mnews.util.myCustomUI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class searchFragment(var context: Context, var home:HomeFragment) {
    val myCustomUI: myCustomUI = myCustomUI(context)
    var searchData:MutableList<beritaModel> = mutableListOf()
    var searchDataCount:MutableState<Int> = mutableStateOf(0)
    var keyword:MutableState<String> = mutableStateOf("")
    var enabled:MutableState<Boolean> = mutableStateOf(true)

    fun searchBerita(key:String){
        if(key.isEmpty()){
            searchData.clear()
            searchDataCount.value = 0
            return
        }
        val clientAPI = clientAPI().getClientAPI();
        val beritaAPI = clientAPI.create(beritaAPI::class.java);
        var resData = beritaAPI.searchBerta(beritaModel.searchModel(key))
        resData.enqueue(object:Callback<ArrayList<beritaModel>>{
            override fun onResponse(
                call: Call<ArrayList<beritaModel>>,
                response: Response<ArrayList<beritaModel>>
            ) {
                if(response.body()!=null){
                    searchData.clear()
                    searchData.addAll(response.body()!!)
                    searchDataCount.value = searchData.size
                }

            }

            override fun onFailure(call: Call<ArrayList<beritaModel>>, t: Throwable) {
            }

        })
    }

    @Composable
    fun searchView(show:MutableState<Boolean>,context: Context){
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)) {
            myCustomUI.MySearchBar(
                modifiers = Modifier.padding(10.dp),
                actions = {
                    searchBerita(keyword.value)
                },
                cancel = {
                    keyword.value = ""
                    show.value = false
                },
                keyword = keyword
            )
            
            Text(modifier = Modifier.padding(horizontal = 10.dp),text = "Menampilkan hasil pencarian ...")

            LazyColumn(Modifier.padding(horizontal=10.dp),content = {
                items(
                    count = searchDataCount.value,
                    itemContent = {
                        myCustomUI.BeritaListView(data = searchData[it], onClickAction = {
                            home.targetBerita.value = searchData[it]
                            home.showDetail.value = true
                        }, onBookMark = {
                            home.addUserBookmark(searchData[it],home.sp.getString("user_id","")!!)
                        })
                    }
                )
            })
        }
    }
}