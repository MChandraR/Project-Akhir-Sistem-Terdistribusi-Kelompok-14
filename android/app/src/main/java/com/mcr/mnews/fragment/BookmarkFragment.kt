package com.mcr.mnews.fragment

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mcr.mnews.interfaces.bookMarkAPI
import com.mcr.mnews.model.beritaModel
import com.mcr.mnews.model.bookMarkModel
import com.mcr.mnews.util.clientAPI
import com.mcr.mnews.util.myCustomUI
import com.mcr.mnews.util.util.colorPalettes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class BookmarkFragment(val context: Context) {
    private val myCustomUI: myCustomUI = myCustomUI(context)
    var bookmarkData:MutableList<beritaModel> = mutableListOf()
    var bookmarkTemp:MutableList<beritaModel> = mutableListOf()
    var bookmarkCount:MutableState<Int> = mutableIntStateOf(bookmarkData.size)
    var showDetail:MutableState<Boolean> = mutableStateOf(false)
    private var keyword:MutableState<String> = mutableStateOf("")
    private var targetBookmark : MutableState<beritaModel> = mutableStateOf(beritaModel())
    private val sp:SharedPreferences = context.getSharedPreferences("mcr",Context.MODE_PRIVATE)

   init {
       getUserBookmark()
   }

    @Composable
    fun BookmarkView(){
        var color  = MaterialTheme.colorScheme.inverseSurface

        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
        ) {
            val (search, container, detail) = createRefs()

            myCustomUI.MySearchBar(
                modifiers = Modifier
                    .padding(10.dp)
                    .constrainAs(search) {
                        top.linkTo(parent.top); bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start); end.linkTo(parent.end)
                    },
                actions = {
                    searchUserBookmark(sp.getString("user_id","")!!,keyword.value)
                },
                cancel = {
                    Toast.makeText(context, keyword.value , Toast.LENGTH_SHORT).show()
                },
                keyword = keyword
            )

            BookMarkListView(
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .constrainAs(container) {
                        top.linkTo(parent.top, 50.dp)
                        start.linkTo(parent.start); end.linkTo(parent.end)
                    }
            )

            AnimatedVisibility(
                modifier  = Modifier.constrainAs(detail){
                    start.linkTo(parent.start); end.linkTo(parent.end)
                    top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                },
                visible = showDetail.value,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                myCustomUI.BeritaDetailView(onClickAction = {
                    showDetail.value  = false
                },dataBerita = targetBookmark)
            }
        }
    }

    fun getUserBookmark(){
        val clientAPI = clientAPI().getClientAPI()
        val bookmarkAPI = clientAPI.create(bookMarkAPI::class.java)
        val resData = bookmarkAPI.getUserBookmark(
            bookMarkModel(
                beritaModel(),
            sp.getString("user_id","")!!)
        )

        resData.enqueue(object:Callback<bookMarkModel>{
            override fun onResponse(call: Call<bookMarkModel>, response: Response<bookMarkModel>) {
                if(response.body()!=null){
                    bookmarkData.clear()
                    bookmarkData.addAll(response.body()!!.data)
                    bookmarkTemp = bookmarkData
                    bookmarkCount.value = bookmarkData.size
                }
            }

            override fun onFailure(call: Call<bookMarkModel>, t: Throwable) {

            }

        })
    }

    fun searchUserBookmark(user_id:String, keyword:String){
        val clientAPI = clientAPI().getClientAPI()
        val bookmarkAPI = clientAPI.create(bookMarkAPI::class.java)
        val resData = bookmarkAPI.searchBookmark(bookMarkModel.searchModel(user_id,keyword))

        resData.enqueue(object:Callback<bookMarkModel.bookmarkResult>{
            override fun onResponse(
                call: Call<bookMarkModel.bookmarkResult>,
                response: Response<bookMarkModel.bookmarkResult>
            ) {
                if(response.body()!=null){
                    bookmarkTemp.clear()
                    bookmarkTemp.addAll(response.body()!!.data)
                    bookmarkCount.value = bookmarkTemp.size
                }
            }

            override fun onFailure(call: Call<bookMarkModel.bookmarkResult>, t: Throwable) {
                Toast.makeText(context,"Gagal terhubung ke server !" + t.toString(),Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun deleteUserBookmark(id:String, idx:Int){
        val clientAPI = clientAPI().getClientAPI()
        val bookmarkAPI = clientAPI.create(bookMarkAPI::class.java)
        val resData = bookmarkAPI.removeBookmark(bookMarkModel.removeModel(id))

        resData.enqueue(object:Callback<bookMarkModel.removeModel>{
            override fun onResponse(call: Call<bookMarkModel.removeModel>, response: Response<bookMarkModel.removeModel>) {
                if(context!=null)Toast.makeText(context,"Berhasil menghapus data bookmark !",Toast.LENGTH_SHORT).show()
                bookmarkData.removeAt(idx)
                bookmarkTemp = bookmarkData
                bookmarkCount.value = bookmarkData.size
            }

            override fun onFailure(call: Call<bookMarkModel.removeModel>, t: Throwable) {
                if(context!=null)Toast.makeText(context,"Gagal menghapus data bookmark !",Toast.LENGTH_SHORT).show()

            }

        })
    }

    private fun showAction(){
        Toast.makeText(context,keyword.value,Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun BookMarkListView(modifier: Modifier){
        Column (modifier = modifier){
            Text(modifier = Modifier.padding(vertical = 10.dp),text = "List bookmark anda " , fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
            LazyColumn(
                state = rememberLazyListState(),
                content = {
                    items(
                        count = bookmarkCount.value,
                        key = {
                            bookmarkTemp[it].judul + it.toString()
                        },
                        itemContent = {
                            bookmarkTemp[it].bookmarked = true
                            myCustomUI.BeritaListView(data = bookmarkTemp[it],{
                                showDetail.value = true
                                targetBookmark.value = bookmarkTemp[it]
                            }, onBookMark = {
                                deleteUserBookmark(bookmarkTemp[it].mark_id,it)
                            })
                        }
                    )
                    item{
                        Box(modifier = Modifier.height(120.dp))
                    }
                }
            )
        }
    }


}