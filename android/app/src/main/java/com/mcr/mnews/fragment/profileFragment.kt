package com.mcr.mnews.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.mcr.mnews.MainMenu
import com.mcr.mnews.R
import com.mcr.mnews.interfaces.userAPI
import com.mcr.mnews.model.LoginModel
import com.mcr.mnews.model.UserModel
import com.mcr.mnews.util.ColorPalettes
import com.mcr.mnews.util.Dialog
import com.mcr.mnews.util.ProgressRequestBody
import com.mcr.mnews.util.clientAPI
import com.mcr.mnews.util.util.colorPalettes
import com.mcr.mnews.util.myCustomUI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class profileFragment(val context: Context, val main: MainMenu) : ProgressRequestBody.UploadCallbacks{
    var showPicture:MutableState<Boolean> = mutableStateOf(false)
    val myCustomUI: myCustomUI = myCustomUI(context)
    val dialog: Dialog = Dialog()

    var SP :SharedPreferences = context.getSharedPreferences("mcr",Context.MODE_PRIVATE)
    var SPEditor :SharedPreferences.Editor = context.getSharedPreferences("mcr",Context.MODE_PRIVATE).edit()
    var isShowingProfile:Boolean = false
    var inProgress:Boolean = false
    //File upload progress
    var uploadProgress:MutableState<Int> = mutableStateOf(0)
    var showProgress:MutableState<Boolean>  = mutableStateOf(false)

    var imgCurUpdate:MutableState<Int> = mutableStateOf(0)
    var username:MutableState<String> = mutableStateOf("")
    var email:MutableState<String> = mutableStateOf("")
    private var isProfileBg: MutableState<Boolean> = mutableStateOf(false)
    var isUpdating: MutableState<Boolean> = mutableStateOf(false)
    var showSettingMenu:MutableState<Boolean> = mutableStateOf(true)
    var showPass:MutableState<Boolean> = mutableStateOf(true)
    var imageKey:MutableState<String> = mutableStateOf(System.currentTimeMillis().toString())

    var profileUrl : MutableState<String> = mutableStateOf("")
    var curTheme : MutableState<String> = mutableStateOf("")

    var showThemeSettting: MutableState<Boolean > = mutableStateOf(false)
    var showDialog : MutableState<Boolean> = mutableStateOf(false)
    var showInfo : MutableState<Boolean> = mutableStateOf(false)
    var dialogButton:ArrayList<myCustomUI.ButtonModel> = ArrayList()
    var curButton:MutableState<ArrayList<myCustomUI.ButtonModel>> = mutableStateOf(ArrayList())
    var dialogModel:MutableState<myCustomUI.DialogModel> = mutableStateOf(dialog.dialogSuccess)

    init {
        getProfileUrl()
        dialogButton.add(
            com.mcr.mnews.util.myCustomUI.ButtonModel(
                {
                    showDialog.value = false
                },
                bg = colorPalettes().darkBlues,
                txt = Color.White,
                "Oke", Modifier.fillMaxWidth()
            )
        )
        curButton.value.add(dialogButton[0])
    }

    private fun logOut(){
        SPEditor.remove("user_id")
        SPEditor.remove("username")
        SPEditor.remove("password")
        SPEditor.remove("email")
        SPEditor.apply()
        main.finish()
    }

    fun getProfileUrl(){
        val clientAPI = clientAPI().getClientAPI()
        val userAPI = clientAPI.create(userAPI::class.java)
        val resData = userAPI.getProfileUrl(clientAPI().serverURL + "img/users/profile/" + SP.getString("user_id","")!! + ".png")

        resData.enqueue(object:Callback<UserModel.ProfileModel>{
            override fun onResponse(
                call: Call<UserModel.ProfileModel>,
                response: Response<UserModel.ProfileModel>
            ) {
                if(response.body()!=null){
                    if(response.body()!!.profile_url!=null){
                        profileUrl.value = response.body()!!.profile_url
                    }
                }
            }

            override fun onFailure(call: Call<UserModel.ProfileModel>, t: Throwable) {

            }
        })
    }

    fun updateUserProfile(data:LoginModel.updateModel){
        isUpdating.value = true
        val clientAPI = clientAPI().getClientAPI()
        val userAPI = clientAPI.create(userAPI::class.java)
        val resData = userAPI.updateProfile(data)

        resData.enqueue(object:Callback<LoginModel.updateModel>{
            override fun onResponse(
                call: Call<LoginModel.updateModel>,
                response: Response<LoginModel.updateModel>
            ) {
                var isNull : Boolean = false
                if(response.body()==null){
                    SPEditor.apply()
                    isUpdating.value = false
                    curButton.value.clear()
                    curButton.value.add(dialogButton[0])
                    dialogModel.value = dialog.dialogUpdateUserError
                    showDialog.value = true
                }else{
                    if(response.body()!!.data!=null){
                        if(response.body()!!.data.username.isNotEmpty()){
                            username.value  = response.body()!!.data.username
                            SPEditor.putString("username",response.body()!!.data.username)
                        }
                    }else isNull = true

                    if(response.body()!!.data!=null){
                        if(response.body()!!.data.email.isNotEmpty()){
                            email.value = response.body()!!.data.email
                            SPEditor.putString("email",response.body()!!.data.email)
                        }
                    }else isNull = true
                    SPEditor.apply()
                    isUpdating.value = false
                    curButton.value.clear()
                    curButton.value.add(dialogButton[0])
                    dialogModel.value = when {
                        isNull -> dialog.dialogProfileError
                        else -> dialog.dialogProfileSuccess
                    }
                    showDialog.value = true
                }

            }

            override fun onFailure(call: Call<LoginModel.updateModel>, t: Throwable) {
                curButton.value.clear()
                curButton.value.add(dialogButton[0])
                dialogModel.value = dialog.dialogRto
                showDialog.value = true

                isUpdating.value = false
            }

        })
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    @Composable
    fun ProfileView(){
        val imageSize = 120
        SP  = context.getSharedPreferences("mcr",Context.MODE_PRIVATE)
        username.value = SP.getString("username","")!!
        email.value = SP.getString("email","")!!
        ConstraintLayout(
            modifier = Modifier
                .background(colorPalettes().darkBlues)
        ) {
            val (image,content,bgBox,blend,dialog,info,progress) = createRefs()

            AnimatedVisibility(
                modifier = Modifier
                    .constrainAs(blend){
                        top.linkTo(parent.top); bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start); end.linkTo(parent.end)
                    },
                visible = !isProfileBg.value,
                enter = fadeIn(animationSpec = tween(
                    durationMillis = 1000
                )),
                exit = fadeOut(animationSpec = tween(
                    durationMillis = 1500
                ))
            ) {
                Column {
                    Box(
                        Modifier
                            .weight(.4f)
                            .fillMaxSize()
                            .background(colorPalettes().darkBlues)
                            .graphicsLayer { alpha = .99f }
                            .drawWithContent {
                                val colors = listOf(
                                    colorPalettes().darkBlues,
                                    Color.Black
                                )
                                drawContent()
                                drawRoundRect(
                                    blendMode = BlendMode.SrcOut,
                                    brush = Brush.verticalGradient(colors = colors),
                                )
                            },
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .weight(.6f)
                            .background(Color.Black))
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(50, 0, 0, 0)
                    )
                    .background(Color.Transparent)
                    .constrainAs(content) {
                        top.linkTo(
                            image.bottom,
                            margin = -(imageSize / 2).dp
                        );start.linkTo(parent.start);end.linkTo(parent.end)
                    }
            ) {

                AnimatedVisibility(
                    visible = showSettingMenu.value,
                    enter = slideInVertically (animationSpec = tween(
                        durationMillis = 1000
                    )
                    ) { it },
                    exit = slideOutVertically(animationSpec = tween(
                        durationMillis = 1500
                    )
                    ) { it }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(top = ((imageSize / 2) + 10).dp, start = 20.dp, end = 20.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(textAlign = TextAlign.Center, text = username.value, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(textAlign = TextAlign.Center, text = email.value, color = Color.Black, fontSize = 15.sp)
                        myCustomUI.MyButton(Icons.Default.Person, "Profil & Privasi",Modifier.padding(top=50.dp, bottom = 10.dp, start = 10.dp, end= 10.dp)){
                            if(!isShowingProfile && !inProgress){
                                showSettingMenu.value =false
                                showPicture.value = false
                                isProfileBg.value = true
                                inProgress = true
                                setTimeOut(action= {
                                    inProgress  = false
                                    isShowingProfile = true
                                },duration=1500)
                            }
                        }
                        myCustomUI.MyButton(Icons.Default.Settings, "Pengaturan", Modifier.padding(10.dp)){
                            showThemeSettting.value = !showThemeSettting.value
                        }
                        themeSetting()
                        myCustomUI.MyButton(Icons.Default.Info, "About", Modifier.padding(10.dp)){
                            showInfo.value = true
                        }
                        myCustomUI.MyButton(Icons.Default.ExitToApp, "Logout", Modifier.padding(10.dp)) {
                            logOut()
                            Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }



            //Bagian Background Profile
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(bgBox) {
                        top.linkTo(parent.top); end.linkTo(parent.end); start.linkTo(parent.start)
                    },
                visible = isProfileBg.value,
                enter = fadeIn(animationSpec = tween(
                    delayMillis = 500,
                    durationMillis = 1000
                )),
                exit = fadeOut(animationSpec = tween(
                    durationMillis = 1000
                ))
            ) {
                ConstraintLayout {
                    val (bg,input) = createRefs()
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .constrainAs(bg) {
                                top.linkTo(parent.top);start.linkTo(parent.start);end.linkTo(parent.end)
                            }
                    ) {

                        GlideImage(
                            modifier =
                            Modifier
                                .weight(.7f)
                                .fillMaxWidth()
                                .graphicsLayer { alpha = 0.99f }
                                .drawWithContent {
                                    val colors = listOf(
                                        Color.Black,
                                        Color.Transparent
                                    )
                                    drawContent()
                                    drawRoundRect(
                                        blendMode = BlendMode.DstAtop,
                                        brush = Brush.verticalGradient(colors = colors),
                                    )
                                },
                            model = profileUrl.value,
                            contentDescription =
                            when{
                                imgCurUpdate.value>-1 -> imgCurUpdate.value.toString()
                                else -> imgCurUpdate.value.toString()
                            }
                        ){
                            it.centerCrop()
                            it.signature(ObjectKey(imageKey.value))
                        }

                        Box(Modifier.weight(.3f))
                    }


                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .constrainAs(input) {
                                top.linkTo(
                                    parent.top
                                );start.linkTo(parent.start);end.linkTo(parent.end)
                            }
                            .background(Color.Transparent)
                            .clip(RoundedCornerShape(15, 15, 15, 15))
                    ) {
                        Column (
                            Modifier.weight(.25f)
                        ){}
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(50.dp)
                                .background(Color.Transparent),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            val textColor = TextFieldDefaults.textFieldColors(
                                textColor = Color.Black,
                                cursorColor = Color.White,
                                disabledTextColor = ColorPalettes().invisibles,
                                focusedIndicatorColor = ColorPalettes().BlueLight,
                                unfocusedIndicatorColor = ColorPalettes().invisibles,
                                disabledIndicatorColor = ColorPalettes().invisibles,
                                containerColor = colorPalettes().dark25
                            )
                            val textShape = RoundedCornerShape(50, 50, 50, 50)
                            var password by remember { mutableStateOf(SP.getString("password","")!!)}
                            Button(colors = ButtonDefaults.buttonColors(colorPalettes().white10),onClick = { main.uploadProfile() }) {
                                Text(text = "Ubah Foto", color = Color.White)
                            }

                            Text(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp, bottom = 15.dp),textAlign = TextAlign.Center,text="Update Profile",fontWeight = FontWeight.Bold, fontSize = 18.sp,color = Color.White)
                            Text(modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Start,text="Email : ",fontWeight = FontWeight.Bold, fontSize = 14.sp,color = Color.White)
                            myCustomUI.MyTextFields(
                                fontColor = Color.White,
                                shape=textShape,
                                colors=textColor,
                                value = if(email.value != null)email.value else "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 15.dp)
                                    .background(ColorPalettes().invisibles),
                                onValueChange = {email.value = it},
                                leadingIcon = {
                                    Icon(painter = painterResource(id = R.drawable.baseline_email_24), contentDescription = "Password", tint = Color.White)
                                },placeholder = {
                                    myCustomUI.PlaceHolder( if (email.value!!.isEmpty()) "Email tidak boleh kosong " else "")
                                }
                            )

                            Text(modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Start,text="Username : ",fontWeight = FontWeight.Bold, fontSize = 14.sp,color = Color.White)
                            myCustomUI.MyTextFields(
                                colors=textColor,
                                fontColor = Color.White,
                                shape=textShape,
                                value = if(username.value!=null)username.value else "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 15.dp)
                                    .background(ColorPalettes().invisibles),
                                onValueChange = {username.value = it},
                                leadingIcon = {
                                    Icon(painter = painterResource(id = R.drawable.baseline_person_outline_24), contentDescription = "Password", tint = Color.White)
                                }, placeholder = {
                                    myCustomUI.PlaceHolder( if (username.value!!.isEmpty()) "Username tidak boleh kosong " else "")
                                }
                            )

                            Text(modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Start,text="Password : ",fontWeight = FontWeight.Bold, fontSize = 14.sp,color = Color.White)
                            myCustomUI.MyTextFields(
                                fontColor = Color.White,
                                shape=textShape,
                                colors=textColor,
                                value = password,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 15.dp)
                                    .background(ColorPalettes().invisibles),
                                onValueChange = {password=it},
                                leadingIcon = {
                                    Icon(painter = painterResource(id = R.drawable.baseline_vpn_key_24), contentDescription = "Password",tint =Color.White)
                                },
                                trailingIcon = {
                                    Icon(
                                        tint = Color.White,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50, 50, 50, 50))
                                            .background(if (showPass.value) colorPalettes().white10 else Color.Transparent)
                                            .clickable { showPass.value = !showPass.value },
                                        painter = painterResource(id = if(showPass.value) R.drawable.baseline_remove_red_eye_24 else R.drawable.baseline_visibility_off_24),
                                        contentDescription = "Password")
                                },
                                passwordVisible = showPass,
                                placeholder = {
                                    myCustomUI.PlaceHolder( if (password.isEmpty()) "Isi untuk mengubah " else "")
                                }
                            )

                            Row(horizontalArrangement = Arrangement.Center) {
                                Button(
                                    colors = ButtonDefaults.buttonColors(Color.Yellow),
                                    modifier = Modifier,
                                    onClick =  {
                                        if(isShowingProfile && !inProgress){
                                            inProgress = true
                                            setTimeOut(action= {
                                                isShowingProfile = false
                                                inProgress = false
                                            },duration=1500)
                                            resetState()
                                        }
                                    }
                                ) {
                                    Text(text="Close", color = colorPalettes().darkBlues)
                                }

                                Button(
                                    enabled= !isUpdating.value,
                                    colors = ButtonDefaults.buttonColors(Color.Yellow),
                                    modifier = Modifier.padding(start=10.dp),
                                    onClick =  {
                                        if(username.value.isEmpty() || email.value.isEmpty()){
                                            Toast.makeText(context,"Username atau email tidak boleh kosong !",Toast.LENGTH_SHORT).show()
                                        }else{
                                            updateUserProfile(LoginModel.updateModel(
                                                SP.getString("user_id","")!!,
                                                username.value,
                                                password,
                                                email.value,
                                                if(password.isEmpty()) 0 else 1
                                            ))
                                        }

                                    }
                                ) {
                                    Text(text="Simpan", color = colorPalettes().darkBlues)
                                }
                            }


                        }

                        Box(Modifier.weight(.1f))
                    }
                }

            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(image) {
                        top.linkTo(
                            parent.top,
                            margin = 150.dp
                        );start.linkTo(parent.start);end.linkTo(parent.end)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){

                AnimatedVisibility(
                    visible = showPicture.value,
                    enter =
                        slideInVertically(
                            animationSpec =  tween(
                                durationMillis = 1500
                            )
                        ) + fadeIn(
                            animationSpec =  tween(
                                durationMillis = 1500
                            )
                        ) ,
                    exit =
                        slideOutVertically(
                            animationSpec =  tween(
                                durationMillis = 1500
                            )
                        ) + fadeOut(
                            animationSpec =  tween(
                                durationMillis = 1500
                            )
                        ) + shrinkOut(
                            animationSpec =  tween(
                                durationMillis = 1500
                            ),
                            clip =false,
                            shrinkTowards = Alignment.Center,
                            targetSize = {IntSize(imageSize,imageSize+100) }
                        ),
                ) {

                    GlideImage(
                        model = profileUrl.value ,
                        modifier = Modifier
                            .clip(CircleShape)
                            .width(imageSize.dp)
                            .height(imageSize.dp)
                            .clickable {
                                if(!isShowingProfile && !inProgress){
                                    showSettingMenu.value =false
                                    showPicture.value = false
                                    isProfileBg.value = true
                                    inProgress = true
                                    setTimeOut(action= {
                                        inProgress  = false
                                        isShowingProfile = true
                                    },duration=1500)
                                }
                            },
                        contentDescription =
                        when{
                            imgCurUpdate.value>-1 -> imgCurUpdate.value.toString()
                            else -> imgCurUpdate.value.toString()
                        }
                    ){
                        it.centerCrop()
                        it.signature(ObjectKey(imageKey.value))

                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.constrainAs(info){
                    top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start);end.linkTo(parent.end)
                },
                visible = showInfo.value,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                AboutFragment().aboutView { showInfo.value = false }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorPalettes().darks10)
                    .constrainAs(dialog) {
                        top.linkTo(parent.top); bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start); end.linkTo(parent.end)
                    },
                visible = showDialog.value,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Column(
                    modifier = Modifier
                        .clickable(true, onClick = {}),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    myCustomUI.myDialog(dialog = dialogModel.value, buttons = curButton.value)
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10,10,0,0)),
                visible = showProgress.value,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .background(colorPalettes().dark25)
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 10.dp) ,
                        text = "Mengunggah file ke server ("+uploadProgress.value+"%) !",
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Box(modifier = Modifier.fillMaxWidth(uploadProgress.value.toFloat() / 100f).height(5.dp).background(colorPalettes().LightBlue))
                }
            }
        }

    }

    fun setUp(){
    }

    @Composable
    fun themeSetting(){
        AnimatedVisibility(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 10.dp)
                .padding(horizontal = 15.dp)
                .padding(start = 25.dp)
                .fillMaxWidth(),
            visible = showThemeSettting.value,
            enter = expandVertically { it },
            exit = shrinkVertically { it }
        ) {
            Column {
                curTheme.value = SP.getString("theme","")!!
                Text(modifier = Modifier.padding(vertical = 5.dp), text = "Pengaturan Tema",color = colorPalettes().darkBlues, fontWeight = FontWeight.Bold)
                Text(modifier = Modifier.padding(bottom = 5.dp), text = "Tema sekarang : " + if(curTheme.value.equals("system")) "auto" else curTheme.value ,color = colorPalettes().darkBlues)

                ConstraintLayout(Modifier.fillMaxWidth()){
                    val (btn1,btn2,btn3) = createRefs()
                    Button(modifier = Modifier.constrainAs(btn1){start.linkTo(parent.start);end.linkTo(btn2.start)}, colors = ButtonDefaults.buttonColors(Color.Gray),onClick = {   setTheme("system")  }) {
                        Text(text = "Auto", color = Color.White)
                    }
                    Button(modifier = Modifier.constrainAs(btn2){start.linkTo(btn1.end);end.linkTo(btn3.start)}, colors = ButtonDefaults.buttonColors(Color.Yellow),onClick = { setTheme("light") }) {
                        Text(text = "Light", color = colorPalettes().darkBlues)
                    }
                    Button(modifier = Modifier.constrainAs(btn3){start.linkTo(btn2.end);end.linkTo(parent.end)}, colors = ButtonDefaults.buttonColors(colorPalettes().darkBlues),onClick = { setTheme("dark")  }) {
                        Text(text = "Dark", color = Color.Yellow)
                    }
                }
            }
        }

    }


    fun setTheme(theme:String){
        SPEditor.putString("theme",theme)
        SPEditor.apply()

        curButton.value.clear()
        curButton.value.add(dialogButton[0])
        dialogModel.value = dialog.dialogThemeSuccess
        showDialog.value = true
        curTheme.value = theme
    }


    fun setTimeOut(action:() -> Unit ,duration:Long){
        Handler(Looper.getMainLooper()).postDelayed({
            action()
        },duration)
    }

    fun resetState(){
        showSettingMenu.value =true
        showPicture.value = true
        isProfileBg.value = false
        showInfo.value = false
    }

    fun toggleProfile(time:Long, state:Boolean){
        inProgress = true
        setTimeOut(action= {
            isShowingProfile = false
            inProgress = false
        },duration=1500)
        resetState()
       android.os.Handler(Looper.getMainLooper()).postDelayed(Runnable {
               showPicture.value = state
       }, time)
    }

    override fun onProgressUpdate(percentage: Int) {
        uploadProgress.value = percentage
    }

    override fun onError() {

    }

    override fun onFinish() {
        uploadProgress.value = 100
    }
}
