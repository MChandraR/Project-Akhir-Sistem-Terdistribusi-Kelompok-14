package com.mcr.mnews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mcr.mnews.ui.theme.MNewsTheme
import com.mcr.mnews.interfaces.userAPI
import com.mcr.mnews.model.LoginModel
import com.mcr.mnews.util.Dialog
import com.mcr.mnews.util.clientAPI
import com.mcr.mnews.util.util.colorPalettes
import com.mcr.mnews.util.myCustomUI
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class SignUpMenu : ComponentActivity() {
    val context: Context = this
    val dialog: Dialog = Dialog()

    var btnEnabled: MutableState<Boolean> = mutableStateOf(true)
    private val myCustomUI: myCustomUI = myCustomUI(context)
    var showPass : MutableState<Boolean> = mutableStateOf(true)

    var showDialog : MutableState<Boolean> = mutableStateOf(false)
    var dialogButton:ArrayList<myCustomUI.ButtonModel> = ArrayList()
    var curButton:MutableState<ArrayList<myCustomUI.ButtonModel>> = mutableStateOf(ArrayList())
    var dialogModel:MutableState<myCustomUI.DialogModel> = mutableStateOf(dialog.dialogSuccess)


    override fun onCreate(savedInstanceState: Bundle?) {
        dialogButton.add(
            com.mcr.mnews.util.myCustomUI.ButtonModel(
                {
                    startActivity(Intent(context, SignInMenu::class.java))
                    showDialog.value = false
                },
                bg = colorPalettes().darkBlues,
                txt = Color.White,
                "Oke", Modifier.fillMaxWidth()
            )
        )
        curButton.value.add(dialogButton[0])
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

        super.onCreate(savedInstanceState)
        setContent {
            SignUpView(this)
        }
    }

    private fun signUp(username:String, password:String, email:String){
        val clientAPI: Retrofit = clientAPI().getClientAPI()
        val userAPI: userAPI = clientAPI.create(userAPI::class.java)
        val userData: LoginModel.loginData = LoginModel.loginData()
        userData.username = username
        userData.password = password
        userData.email = email
        val request  = userAPI.signUp(userData)

        request.enqueue(object:retrofit2.Callback<LoginModel>{
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                val resData = response.body()!!
                if(resData.status=="sukses"){
                    curButton.value.clear()
                    curButton.value.add(dialogButton[0])
                    dialogModel.value = dialog.dialogRegistSuccess
                    showDialog.value = true
                }else{
                    //Toast.makeText(context,resData.message, Toast.LENGTH_SHORT).show()
                    curButton.value.clear()
                    curButton.value.add(dialogButton[1])
                    dialogModel.value = dialog.dialogRegistError
                    showDialog.value = true
                }
                btnEnabled.value = true
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                curButton.value.clear()
                curButton.value.add(dialogButton[1])
                dialogModel.value = dialog.dialogRto
                showDialog.value = true

                btnEnabled.value = true
            }

        })


    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SignUpView(context:Context){
        MNewsTheme(colorPalettes().blue,context) {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorPalettes().blue
            ) {
                var email by remember {
                    mutableStateOf("")
                }
                var username by remember {
                    mutableStateOf("")
                }
                var password by remember {
                    mutableStateOf("")
                }
                ConstraintLayout {
                    val (main,dialog) = createRefs()

                    Column(
                        modifier = Modifier
                            .padding(horizontal =  50.dp)
                            .constrainAs(main){
                                top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start);end.linkTo(parent.end)
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(.4f),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                            Text(text="Create Account", color = Color.White,textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold, fontSize = 48.sp, lineHeight = 52.sp, fontFamily = FontFamily.SansSerif)
                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(.6f), horizontalAlignment = Alignment.CenterHorizontally){
                            Text(text="Email", textAlign = TextAlign.Left, color = Color.White, modifier = Modifier.fillMaxWidth())
                            myCustomUI.MyTextFields(value = email ,
                                onValueChange = {email = it},
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    disabledTextColor = colorPalettes().invisibles,
                                    focusedIndicatorColor = colorPalettes().BlueLight,
                                    unfocusedIndicatorColor = colorPalettes().invisibles,
                                    disabledIndicatorColor = colorPalettes().invisibles,
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(50, 50, 50, 50),
                                modifier  = Modifier
                                    .padding(bottom = 10.dp)
                                    .background(colorPalettes().invisibles),
                                leadingIcon = {
                                    Icon(painter = painterResource(id = R.drawable.baseline_email_24), contentDescription = "Password")
                                },
                                placeholder = {
                                    myCustomUI.PlaceHolder("Masukkan email")
                                })

                            Text(text="Username", textAlign = TextAlign.Left, color = Color.White, modifier = Modifier.fillMaxWidth())
                            myCustomUI.MyTextFields(value = username ,
                                onValueChange = {username = it},
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    disabledTextColor = colorPalettes().invisibles,
                                    focusedIndicatorColor = colorPalettes().BlueLight,
                                    unfocusedIndicatorColor = colorPalettes().invisibles,
                                    disabledIndicatorColor = colorPalettes().invisibles,
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(50, 50, 50, 50),
                                modifier  = Modifier
                                    .padding(bottom = 10.dp)
                                    .background(colorPalettes().invisibles),
                                leadingIcon = {
                                    Icon(painter = painterResource(id = R.drawable.baseline_person_outline_24), contentDescription = "Password")
                                },
                                placeholder = {
                                    myCustomUI.PlaceHolder("Masukkan username")
                                })


                            Text(text="Password", textAlign = TextAlign.Left, color = Color.White,modifier = Modifier.fillMaxWidth())
                            myCustomUI.MyTextFields(value = password ,
                                onValueChange = {password = it},
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    disabledTextColor = colorPalettes().invisibles,
                                    focusedIndicatorColor = colorPalettes().BlueLight,
                                    unfocusedIndicatorColor = colorPalettes().invisibles,
                                    disabledIndicatorColor = colorPalettes().invisibles,
                                    containerColor = Color.White
                                ),
                                passwordVisible = showPass,
                                shape = RoundedCornerShape(50,50,50,50),
                                modifier  = Modifier
                                    .padding(bottom = 30.dp)
                                    .background(colorPalettes().invisibles),
                                leadingIcon = {
                                    Icon(painter = painterResource(id = R.drawable.baseline_vpn_key_24), contentDescription = "Password")
                                },
                                trailingIcon = {
                                    Icon(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50, 50, 50, 50))
                                            .background(if (showPass.value) colorPalettes().white10 else Color.Transparent)
                                            .clickable { showPass.value = !showPass.value },
                                        painter = painterResource(id = if(showPass.value) R.drawable.baseline_remove_red_eye_24 else R.drawable.baseline_visibility_off_24),
                                        contentDescription = "Password")
                                },
                                placeholder = {
                                    myCustomUI.PlaceHolder("Masukkan password")
                                })


                            Button(
                                enabled = btnEnabled.value,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp),colors =  ButtonDefaults.buttonColors(
                                    colorPalettes().yellow),
                                onClick = {
                                    if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
                                        curButton.value.clear()
                                        curButton.value.add(dialogButton[1])
                                        dialogModel.value = Dialog().dialogRegistReject
                                        showDialog.value = true
                                    }else{
                                        btnEnabled.value = false
                                        signUp(username, password,email)
                                    }

                                },
                                shape = RoundedCornerShape(20,20,20,20)
                            ){
                                Text(text="Create Account",color = colorPalettes().darkBlues)
                            }

                            Text(text = "Already have an account  ?", color = Color.White, fontSize = 13.sp)

                            TextButton(
                                onClick = {
                                    startActivity(Intent(context, SignInMenu::class.java))
                                },
                                modifier = Modifier.offset(y= (-13).dp)
                            ) {
                                Text(text = "Sign in", color = Color.Yellow,fontSize = 13.sp)
                            }

                        }
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
                }

            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(context,MainActivity::class.java))
    }
}