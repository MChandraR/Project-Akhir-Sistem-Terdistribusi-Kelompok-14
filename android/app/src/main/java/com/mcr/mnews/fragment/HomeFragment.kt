package com.mcr.mnews.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mcr.mnews.R
import com.mcr.mnews.interfaces.beritaAPI
import com.mcr.mnews.interfaces.bookMarkAPI
import com.mcr.mnews.model.beritaModel
import com.mcr.mnews.model.bookMarkModel
import com.mcr.mnews.util.clientAPI
import com.mcr.mnews.util.util.colorPalettes
import com.mcr.mnews.util.myCustomUI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment(var context: Context) {
    private val category = listOf("Terbaru","Politik","Tech","Lifestyle")
    private val myCustomUI: myCustomUI = myCustomUI(context)
    private var selectedCategory:MutableState<String> = mutableStateOf(category[0])
    private var columnSize = mutableStateOf(IntSize.Zero)
    val searchFragment: searchFragment = searchFragment(context,this)
    var scrollStat:LazyListState = LazyListState()
    var showDetail:MutableState<Boolean> = mutableStateOf(false)
    var showSearch:MutableState<Boolean> = mutableStateOf(false)
    var beritaData : MutableList<beritaModel> = mutableListOf()
    var beritaCount : MutableState<Int> = mutableIntStateOf(beritaData.size)
    var targetBerita: MutableState<beritaModel> = mutableStateOf(beritaModel())
    var sp:SharedPreferences = context.getSharedPreferences("mcr",Context.MODE_PRIVATE)

    fun fetchDataBerita(){
        val clientAPI = clientAPI().getClientAPI()
        val beritaAPI = clientAPI.create(beritaAPI::class.java)
        val resData = beritaAPI.getDataBerita()

        resData.enqueue(object:Callback<ArrayList<beritaModel>>{
            override fun onResponse(
                call: Call<ArrayList<beritaModel>>,
                response: Response<ArrayList<beritaModel>>
            ) {
                beritaData.clear();
                beritaData.addAll(response.body()!!)
                beritaCount.value = beritaData.size
                Handler(Looper.getMainLooper()).postDelayed({
                    getBeritaByCategory(selectedCategory.value)
                },15000)

            }

            override fun onFailure(call: Call<ArrayList<beritaModel>>, t: Throwable) {
                Toast.makeText(context,"Gagal terhubung ke server !",Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    getBeritaByCategory(selectedCategory.value)
                },5000)
            }
        })
    }

    init {
        getBeritaByCategory(category[0])
    }

    private fun getBeritaByCategory(kategori:String){
        val clientAPI = clientAPI().getClientAPI()
        val beritaAPi = clientAPI.create(beritaAPI::class.java)
        val resData = beritaAPi.getBeritabyCategory("api/berita?kategori="+kategori+"&user_id="+sp.getString("user_id","")!!)

        resData.enqueue(object:Callback<java.util.ArrayList<beritaModel>>{
            override fun onResponse(
                call: Call<java.util.ArrayList<beritaModel>>,
                response: Response<java.util.ArrayList<beritaModel>>
            ) {
               if(response.body()!=null){
                   beritaData.clear()
                   beritaData.addAll(response.body()!!)
                   beritaCount.value = beritaData.size
               }
                Handler(Looper.getMainLooper()).postDelayed({
                    getBeritaByCategory(selectedCategory.value)
                },60000)
            }

            override fun onFailure(call: Call<java.util.ArrayList<beritaModel>>, t: Throwable) {
                Handler(Looper.getMainLooper()).postDelayed({
                    getBeritaByCategory(selectedCategory.value)
                },5000)
            }
        }
        )
    }

    private fun getOnceBeritaByCategory(kategori:String){
        val clientAPI = clientAPI().getClientAPI()
        val beritaAPi = clientAPI.create(beritaAPI::class.java)
        val resData = beritaAPi.getBeritabyCategory("api/berita?kategori="+kategori+"&user_id="+sp.getString("user_id","")!!)

        resData.enqueue(object:Callback<java.util.ArrayList<beritaModel>>{
            override fun onResponse(
                call: Call<java.util.ArrayList<beritaModel>>,
                response: Response<java.util.ArrayList<beritaModel>>
            ) {
                if(response.body()!=null){
                    beritaData.clear()
                    beritaData.addAll(response.body()!!)
                    beritaCount.value = beritaData.size
                }

            }

            override fun onFailure(call: Call<java.util.ArrayList<beritaModel>>, t: Throwable) {

            }
        }
        )
    }

     fun addUserBookmark(dataBerita: beritaModel, userid:String){
        val clientAPI = clientAPI().getClientAPI()
        val bookmarkAPI  = clientAPI.create(bookMarkAPI::class.java)
        val resData = bookmarkAPI.addBookMark(bookMarkModel(dataBerita,userid))

        resData.enqueue(object: Callback<bookMarkModel>{
            override fun onResponse(call: Call<bookMarkModel>, response: Response<bookMarkModel>) {
                Toast.makeText(context,"Berhasil menambahkan berita ke bookmark !",Toast.LENGTH_SHORT).show()
                val newTarget: beritaModel = targetBerita.value
                dataBerita.bookmarked = true
                targetBerita.value = dataBerita
                showDetail.value = !showDetail.value
                showDetail.value = !showDetail.value

            }

            override fun onFailure(call: Call<bookMarkModel>, t: Throwable) {
                Toast.makeText(context, "Gagal terhubung ke server !\n$t",Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun startChek(){

    }

    fun getBanner():Int{
        if(beritaCount.value < 1 ) return 0
        else if(beritaCount.value <= 4 ) return beritaCount.value
        else return  5
    }


    @Composable
    fun HomeView(context: Context){
        this.context = context
        ConstraintLayout() {
            val (mainView,detail,search) = createRefs()

            Column(modifier = Modifier
                .fillMaxSize()
                .constrainAs(mainView) {
                    top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start);end.linkTo(parent.end)
                }) {
                Column(modifier = Modifier
                    .weight(.075f)
                    .background(colorPalettes().darkBlues), verticalArrangement = Arrangement.Center) {
                    topBar()
                }
                val den = context.resources.displayMetrics.density

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(.925f)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 10.dp).padding(top=10.dp)
                    .onGloballyPositioned {
                        columnSize.value = it.size
                    }
                ) {
                    Row(Modifier.horizontalScroll(rememberScrollState())){
                        category.forEach {
                            Button(
                                colors = ButtonDefaults.buttonColors(if(it==selectedCategory.value) Color.Yellow else colorPalettes().darkBlues),
                                modifier = Modifier.padding(horizontal = 5.dp),
                                onClick = {
                                    getOnceBeritaByCategory(it)
                                    selectedCategory.value = it
                                }
                            ) {
                                Text(text = it, color = if(it!=selectedCategory.value) Color.White else colorPalettes().darkBlues)
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = remember { derivedStateOf { scrollStat.firstVisibleItemIndex } }.value < 1,
                        enter = expandVertically {-it/2},
                        exit = shrinkVertically { -it/2 }
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .width((columnSize.value.width / den - 0.5f).dp)
                                .padding(top = 10.dp),
                            state = rememberLazyListState(),
                            content = {
                                items(
                                    count = getBanner() ,
                                    itemContent = {
                                        Banner(width = columnSize.value, dataBerita = beritaData[it])
                                    }
                                )
                            }
                        )
                    }


                    Text(modifier = Modifier.padding(bottom=5.dp,top = 10.dp,start = 5.dp),text = "For You",fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.SansSerif, color = MaterialTheme.colorScheme.outline)

                    daftarBerita()

                }
            }
            //Search Area
            AnimatedVisibility(
                modifier  = Modifier
                    .fillMaxSize()
                    .constrainAs(search) {
                        start.linkTo(parent.start); end.linkTo(parent.end)
                        top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                    },
                visible = showSearch.value,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                searchFragment.searchView(showSearch,context)
            }
            //Detail Area
            AnimatedVisibility(
                modifier  = Modifier.constrainAs(detail){
                    start.linkTo(parent.start); end.linkTo(parent.end)
                    top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                },
                visible = showDetail.value,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                myCustomUI.BeritaDetailView(
                    onClickAction = {
                        showDetail.value  = false
                        searchFragment.enabled.value = true
                    },
                    dataBerita = targetBerita,
                    onBookMark = {
                        addUserBookmark(targetBerita.value,
                            sp.getString("user_id","")!!)
                    }

                )
            }
        }


    }

    @Composable
    fun daftarBerita(){
        LazyColumn(
            modifier = Modifier.clickable(enabled = !showDetail.value, onClick = {  }),
            state = scrollStat,
            userScrollEnabled = !(showDetail.value || showSearch.value),
            content = {
                items(
                    count = beritaCount.value,
                    itemContent = {
                        myCustomUI.BeritaListView(data = beritaData[it], onClickAction = {
                            if(!showSearch.value){
                                showDetail.value = true
                                showSearch.value = false
                                searchFragment.enabled.value = false
                            }
                            targetBerita.value = beritaData[it]
                        }, onBookMark = {
                            addUserBookmark(beritaData[it],sp.getString("user_id","")!!)
                        })
                    }
                )
                item {
                    Box(modifier = Modifier.height(80.dp))
                }
            }
        )
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun Banner(width: IntSize,dataBerita: beritaModel){
        val density = this.context!!.resources.displayMetrics.density
        Box{
            ConstraintLayout(modifier = Modifier
                .width((width.width / density - 0.5f).dp)
                .padding(horizontal = 5.dp)) {
                val (bg,content) = createRefs()
                GlideImage(
                    model = if(dataBerita.gambar!=null) dataBerita.gambar else painterResource(id = R.drawable.berita_banner),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(5, 5, 5, 5))
                        .constrainAs(bg) {
                            start.linkTo(parent.start)
                        }
                        .fillMaxWidth()
                        .height(175.dp)
                        .graphicsLayer { alpha = 0.99f }
                        .drawWithContent {
                            val colors = listOf(
                                Color.Black,
                                Color.Transparent
                            )
                            drawContent()
                            drawRoundRect(
                                blendMode = BlendMode.DstOut,
                                brush = Brush.verticalGradient(colors = colors),
                            )
                        }
                )

                Column(
                    modifier = Modifier
                        .padding(top = 30.dp, start = 10.dp)
                        .constrainAs(content) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Text(maxLines = 2, text = if(dataBerita.judul!=null)dataBerita.judul else " ", color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Bold)
                    Text(
                        style = TextStyle(
                            shadow = Shadow(
                                MaterialTheme.colorScheme.inverseSurface,
                                blurRadius = .5f
                            )
                        ),
                        maxLines = 4,
                        modifier = Modifier
                            .padding(top = 5.dp, end = 5.dp)
                            .height(100.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 14.sp,
                        text = if(dataBerita.deskripsi!=null)dataBerita.deskripsi else " ",
                        color = MaterialTheme.colorScheme.outline,
                    )
                }

            }
        }
    }

    @Composable
    fun topBar(){
        Row(modifier = Modifier
            .fillMaxWidth(),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(.15f)) {
                Image(modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 5.dp),painter = painterResource(id = R.drawable.logo_umrah), contentDescription = "User image")
            }
            Column(modifier = Modifier.weight(.7f)) {
                Text(modifier = Modifier.fillMaxWidth(), color=Color.White, text = "DAILY NEWS", textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(.15f)) {
                Image(modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        searchFragment.enabled.value,
                        onClick = { showSearch.value = true }), colorFilter = ColorFilter.tint(Color.White), painter = painterResource(id = R.drawable.baseline_search_24), contentDescription = "User image")
            }
        }
    }

    fun getLazyState():LazyListState{
        return scrollStat
    }
}
