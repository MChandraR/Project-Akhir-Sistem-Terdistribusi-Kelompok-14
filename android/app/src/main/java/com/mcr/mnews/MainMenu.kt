package com.mcr.mnews

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mcr.mnews.fragment.BookmarkFragment
import com.mcr.mnews.fragment.HomeFragment
import com.mcr.mnews.fragment.NotifFragment
import com.mcr.mnews.fragment.profileFragment
import com.mcr.mnews.interfaces.userAPI
import com.mcr.mnews.model.LoginModel
import com.mcr.mnews.ui.theme.MNewsTheme
import com.mcr.mnews.util.ProgressRequestBody
import com.mcr.mnews.util.clientAPI
import com.mcr.mnews.util.menus
import com.mcr.mnews.util.util.colorPalettes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class MainMenu : ComponentActivity() {
    private val context:Context = this
    private val activity:ComponentActivity = this
    private val items = listOf(menus.Home, menus.Notif, menus.Bookmark, menus.Profile)
    //UI Class
    private lateinit var homeFragments: HomeFragment
    private lateinit var profileFragments: profileFragment
    private lateinit var bookmarkFragment: BookmarkFragment
    private lateinit var notifFragment: NotifFragment
    //UI Composalbe view
    lateinit var scrollState:LazyListState
    lateinit var homes: @Composable () -> Unit
    lateinit var profiles:@Composable () -> Unit
    lateinit var bookmarks:@Composable () -> Unit
    lateinit var notifs:@Composable () -> Unit

    private var selectedScreen: MutableState<String> =  mutableStateOf(menus.Home.route)

    override fun onCreate(savedInstanceState: Bundle?) {
        homeFragments  = HomeFragment(context)
        homes = { homeFragments.HomeView(this) }
        homeFragments.startChek()
        profileFragments  = profileFragment(this,this)
        profiles = {profileFragments.ProfileView()}
        bookmarkFragment = BookmarkFragment(this)
        bookmarks = {bookmarkFragment.BookmarkView()}
        scrollState = homeFragments.getLazyState()
        notifFragment = NotifFragment(this)
        notifs = {notifFragment.notifMainView()}

        super.onCreate(savedInstanceState)
        setContent {
            MNewsTheme(color = colorPalettes().darkBlues,this) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    MainMenuView()
                }
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainMenuView(){
        Scaffold(
            bottomBar = {
                BotNavBar( )
            },
            modifier = Modifier.background(Color.White)
        ) {
            val pad = it
            when(selectedScreen.value) {
                "home" -> {
                    homes()
                    profileFragments.toggleProfile(0, false)
                }
                "profile" -> {
                    profiles()
                    profileFragments.setUp()
                    profileFragments.resetState()
                    profileFragments.toggleProfile(200, true)
                }
                "notification" ->{
                    notifs()
                }
                "bookmark" ->{
                    bookmarks()
                    bookmarkFragment.getUserBookmark()
                    profileFragments.toggleProfile(0, false)
                }
            }
        }
    }

    @Composable
    fun BotNavBar(){
        AnimatedVisibility(
            visible = !homeFragments.scrollStat.isScrollInProgress,
            enter = slideInVertically(initialOffsetY = {100}),
            exit = slideOutVertically(targetOffsetY = {100})
        ) {
            MyNavigationBar(containerColor = colorPalettes().blue, modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(50, 50, 50, 50)
                )
                .background(colorPalettes().blue)) {
                items.forEach{ replyDestination ->
                    NavigationBarItem(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(all = 0.dp),
                        selected = selectedScreen.value == "",
                        onClick = {
                            selectedScreen.value = replyDestination.route
                        },
                        icon = {
                            Icon( tint =  if(selectedScreen.value==replyDestination.route) Color.Yellow else Color.White, imageVector =  replyDestination.icon, contentDescription = replyDestination.route )
                        },
                        label = {
                            Text(text = replyDestination.title!!, color = Color.White)
                        }
                    )

                }
            }
        }


    }

    var isQuiting:Boolean = false
    var backCount:Int = 0

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        if (homeFragments.showDetail.value || homeFragments.showSearch.value) {
            homeFragments.showDetail.value = false
            homeFragments.showSearch.value = false
            homeFragments.searchFragment.enabled.value = true
        }else if(bookmarkFragment.showDetail.value){
            bookmarkFragment.showDetail.value = false
        }else{
            if(!isQuiting){
                Toast.makeText(context,"Tekan sekali lagi untuk keluar !", Toast.LENGTH_SHORT).show()
                backCount+=1
                isQuiting = true
                Handler(Looper.getMainLooper()).postDelayed({
                    isQuiting = false
                },1000)
                Handler(Looper.getMainLooper()).postDelayed({
                    backCount = 0
                },3000)
                if(backCount>1){
                    finishAffinity()
                    super.onBackPressed()
                }
            }
        }
    }

    fun uploadProfile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("image/*")

        activity.startActivityForResult(
            Intent.createChooser(intent, "Select a file"),
            111
        )
    }

    @Throws(IOException::class)
    fun readUri(context: Context, uri: Uri?): ByteArray? {
        val pdf = context.contentResolver.openFileDescriptor(uri!!, "r")!!
        assert(pdf.statSize <= Int.MAX_VALUE)
        val data = ByteArray(pdf.statSize.toInt())
        val fd = pdf.fileDescriptor
        val fileStream = FileInputStream(fd)
        fileStream.read(data)
        return data
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile: Uri? = data?.data // The URI with the location of the file
            selectedFile?.let {
                val m = packageManager
                var s = packageName
                try {
                    val p = m.getPackageInfo(s!!, 0)
                    s = p.applicationInfo.dataDir
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.w("yourtag", "Error Package name not found ", e)
                }
                val dir: File = File(dataDir,"data")
                dir.mkdirs()
                val cR = context.contentResolver
                val mime = MimeTypeMap.getSingleton()
                val type = mime.getExtensionFromMimeType(cR.getType(selectedFile))
                val file = File(dir.path,profileFragments.SP.getString("user_id","")!!+".png")
                file.setWritable(true)
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(
                        readUri(context,selectedFile)
                    )
                }
                val FIS: InputStream? = contentResolver.openInputStream(it)

                if (file != null) {
                    profileFragments.showProgress.value = true
                    val reqFile: ProgressRequestBody = ProgressRequestBody(file,"multipart/form-data", profileFragments)
                    val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, reqFile)

                    val clientAPI = clientAPI().getClientAPI()
                    val userAPI = clientAPI.create(userAPI::class.java)
                    val resData = userAPI.updateUserImage(body)

                    resData.enqueue(object : Callback<LoginModel.imageUploadModel> {
                        override fun onResponse(
                            call: Call<LoginModel.imageUploadModel>,
                            response: Response<LoginModel.imageUploadModel>
                        ) {
                            profileFragments.curButton.value.clear()
                            profileFragments.curButton.value.add(profileFragments.dialogButton[0])
                            profileFragments.dialogModel.value = profileFragments.dialog.dialogProfileSuccess
                            profileFragments.showDialog.value = true

                            profileFragments.getProfileUrl()
                            profileFragments.imgCurUpdate.value +=1
                            profileFragments.imageKey.value = System.currentTimeMillis().toString()

                            profileFragments.showProgress.value = false
                        }

                        override fun onFailure(call: Call<LoginModel.imageUploadModel>, t: Throwable) {
                            Toast.makeText(context, "Tidak dapat terhubung ke server!\n"+t.toString(), Toast.LENGTH_SHORT).show()
                            profileFragments.showProgress.value = false
                        }
                    })
                }
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File {
        val file = File(uri.path!!)
        // You may need additional handling here depending on the type of URI
        return file
    }

}




@Composable
fun MyNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .height(70.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            content = content
        )
    }
}

