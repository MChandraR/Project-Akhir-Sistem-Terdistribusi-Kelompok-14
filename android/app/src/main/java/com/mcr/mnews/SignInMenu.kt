package com.mcr.mnews

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mcr.mnews.ui.theme.MNewsTheme
import com.mcr.mnews.util.ColorPalettes
import com.mcr.mnews.interfaces.userAPI
import com.mcr.mnews.model.LoginModel
import com.mcr.mnews.util.Dialog
import com.mcr.mnews.util.clientAPI
import com.mcr.mnews.util.myCustomUI
import com.mcr.mnews.util.myCustomUI.DialogModel
import com.mcr.mnews.util.myCustomUI.ButtonModel
import com.mcr.mnews.util.util.colorPalettes
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class SignInMenu : ComponentActivity() {
    var context: Context = this
    val dialog:Dialog = Dialog()
    var btnEnabled : MutableState<Boolean> = mutableStateOf(true)
    var showPass : MutableState<Boolean> = mutableStateOf(true)
    var showDialog : MutableState<Boolean> = mutableStateOf(false)

    private val myCustomUI: myCustomUI = myCustomUI(context)
    var dialogButton:ArrayList<ButtonModel> = ArrayList()
    var curButton:MutableState<ArrayList<ButtonModel>> = mutableStateOf(ArrayList())
    var dialogModel:MutableState<DialogModel> = mutableStateOf(DialogModel(Color.White,R.drawable.baseline_check_circle_outline_24,Color.Yellow,"Apakah anda yakin ?","Harap konfirmasi !"))

    override fun onCreate(savedInstanceState: Bundle?) {
        dialogButton.add(ButtonModel(
            {
                startActivity(Intent(context, MainMenu::class.java))
                showDialog.value = false
            },
            bg = colorPalettes().darkBlues,
            txt = Color.White,
            "Oke",Modifier.fillMaxWidth()
        ))
        curButton.value.add(dialogButton[0])
        dialogButton.add(com.mcr.mnews.util.myCustomUI.ButtonModel(
            {
                showDialog.value = false
            },
            bg = colorPalettes().darkBlues,
            txt = Color.White,
            "Oke",Modifier.fillMaxWidth()
        ))
        super.onCreate(savedInstanceState)
        context = this
        setContent {
            mainMenu(this)
        }
    }

    private fun signIn(context: Context, username:String, password:String){
        val sp :SharedPreferences.Editor = getSharedPreferences("mcr",Context.MODE_PRIVATE).edit()
        val clientAPI: Retrofit = clientAPI().getClientAPI()
        val userAPI: userAPI = clientAPI.create(userAPI::class.java)
        val reqData: LoginModel.loginData = LoginModel.loginData()
        reqData.username = username
        reqData.password = password
        val request  = userAPI.signIn(reqData)

        request.enqueue(object:retrofit2.Callback<LoginModel>{
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {

                if(response.body()!=null) {
                    val resData = response.body()!!
                    if(resData.status=="sukses"){
                        sp.putString("user_id",resData.data.user_id)
                        sp.putString("username",resData.data.username)
                        sp.putString("email",resData.data.email)
                        sp.apply()
                        dialogModel.value = dialog.dialogSuccess
                        curButton.value.clear()
                        curButton.value.add(dialogButton[0])
                        showDialog.value = true
                    }else{
                        dialogModel.value = dialog.dialogError
                        curButton.value.clear()
                        curButton.value.add(dialogButton[1])
                        showDialog.value = true
                    }
                }

                btnEnabled.value = true
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                dialogModel.value = dialog.dialogRto
                curButton.value.clear()
                curButton.value.add(dialogButton[1])
                showDialog.value = true

                btnEnabled.value = true
            }

        })


    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun mainMenu(context:Context){
        var email by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        MNewsTheme(color = colorPalettes().darkBlues,context) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorPalettes().darkBlues
            ) {
                ConstraintLayout {
                    val (main,dialog) = createRefs()
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 50.dp)
                            .constrainAs(main) {
                                top.linkTo(parent.top); bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start); end.linkTo(parent.end)
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(.5f),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                            Text(modifier = Modifier
                                .clickable {
                                    startActivity(Intent(context, MainMenu::class.java))
                                },
                                text="Welcome back", color = Color.White,textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold, fontSize = 48.sp, lineHeight = 52.sp, fontFamily = FontFamily.SansSerif)
                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(.5f), horizontalAlignment = Alignment.CenterHorizontally){
                            Text(text="Email", textAlign = TextAlign.Left, color = Color.White, modifier = Modifier.fillMaxWidth())
                            myCustomUI.MyTextFields(value = email ,
                                onValueChange = {email = it},
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    disabledTextColor = ColorPalettes().invisibles,
                                    focusedIndicatorColor =ColorPalettes().BlueLight,
                                    unfocusedIndicatorColor = ColorPalettes().invisibles,
                                    disabledIndicatorColor = ColorPalettes().invisibles,
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(50, 50, 50, 50),
                                modifier  = Modifier
                                    .padding(bottom = 10.dp)
                                    .background(ColorPalettes().invisibles),
                                leadingIcon = {
                                    Icon(painter = painterResource(id = R.drawable.baseline_person_outline_24), contentDescription = "Password")
                                },
                                placeholder = {
                                    myCustomUI.PlaceHolder("Masukkan email atau username")
                                })


                            Text(text="Password", textAlign = TextAlign.Left, color = Color.White,modifier = Modifier.fillMaxWidth())
                            myCustomUI.MyTextFields(value = password ,
                                onValueChange = {password = it},
                                passwordVisible = showPass,
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    disabledTextColor = ColorPalettes().invisibles,
                                    focusedIndicatorColor =ColorPalettes().BlueLight,
                                    unfocusedIndicatorColor = ColorPalettes().invisibles,
                                    disabledIndicatorColor = ColorPalettes().invisibles,
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(50, 50, 50, 50),
                                modifier  = Modifier
                                    .padding(bottom = 30.dp)
                                    .background(ColorPalettes().invisibles),
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
                                enabled = btnEnabled.value ,
                                modifier = Modifier.fillMaxWidth(),colors =  ButtonDefaults.buttonColors(
                                    colorPalettes().yellow),
                                onClick = {
                                    btnEnabled.value = false
                                    signIn(context,email,password)
                                },
                                shape = RoundedCornerShape(20,20,20,20)
                            ){
                                Text(text="Login", color = colorPalettes().darkBlues)
                            }

                            Text(text = "Don't have an account yet ?", color = Color.White, fontSize = 13.sp)
                            TextButton(
                                onClick = {
                                    startActivity(Intent(context, SignUpMenu::class.java))
                                },
                                modifier = Modifier.offset(y= (-13).dp)
                            ) {
                                Text(text = "Sign up", color = Color.Yellow,fontSize = 13.sp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    leadingIcon:  @Composable (() -> Unit)? = null,
    trailingIcon:  @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    value:String = "",
    modifier: Modifier,
    passwordVisible:Boolean= true,
    onValueChange: (String) -> Unit,
    colors:androidx.compose.material3.TextFieldColors = TextFieldDefaults.textFieldColors(
        colorPalettes().invisibles),
    shape: Shape = TextFieldDefaults.filledShape
){
    BasicTextField(
        value = value,
        modifier = modifier
            .defaultMinSize(
                minHeight = 16.dp
            )
            .background(colorPalettes().invisibles)
            .fillMaxWidth()
            .padding(all = 0.dp),
        onValueChange = onValueChange,
        enabled = true,
        readOnly = false,
        cursorBrush = SolidColor(colorPalettes().BlueLight),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
        interactionSource = remember { MutableInteractionSource() },
        singleLine = true,
        maxLines = 1,
        decorationBox = {
            TextFieldDefaults.TextFieldDecorationBox(
                contentPadding = PaddingValues(0.dp) ,
                value = value,
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                supportingText = supportingText,
                shape = RoundedCornerShape(50,50,50,50),
                singleLine = true,
                enabled = true,
                isError = false,
                interactionSource = remember { MutableInteractionSource() },
                colors = colors
            )
        }

    )
}
