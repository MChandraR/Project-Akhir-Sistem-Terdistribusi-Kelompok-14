package com.mcr.mnews.util


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mcr.mnews.R
import com.mcr.mnews.model.beritaModel
import com.mcr.mnews.util.util.colorPalettes

class myCustomUI(val context: Context) {
    var buttons:ArrayList<ButtonModel> = ArrayList()
    init {
        buttons.add(ButtonModel({},Color.Yellow,Color.White,"Close"))
        buttons.add(ButtonModel({},colorPalettes().darkBlues,Color.White,"Yes"))
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

    @Composable
    fun PlaceHolder(text:String=""){
        Text(text = text, color = colorPalettes().grays, fontSize = 14.sp)
    }


    @Composable
    fun MyButton(icon: ImageVector, text:String, modifier: Modifier = Modifier, onClickAction: () -> Unit={}){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .clickable(true, onClick = onClickAction),
        ) {
            Row(horizontalArrangement = Arrangement.Start){
                Image(imageVector = icon , colorFilter = ColorFilter.tint(colorPalettes().darkBlues), contentDescription = null)
                Text(modifier = Modifier.padding(start = 5.dp), text = text, color = colorPalettes().darkBlues)
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun BeritaListView(data: beritaModel, onClickAction: () -> Unit, onBookMark: () -> Unit={}){
        Box(modifier = Modifier
            .height(170.dp)
            .padding(5.dp)
            .clickable(onClick = onClickAction)
            .background(Color.Transparent)
            .clip(RoundedCornerShape(10, 10, 10, 10))){
            Row(
                Modifier
                    .background(MaterialTheme.colorScheme.secondary)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier
                    .padding(20.dp)
                    .weight(.6f)
                    .fillMaxWidth()) {
                    Text(lineHeight = 14.sp, maxLines = 2, modifier = Modifier.padding(bottom=5.dp),text = if(data.judul!=null)data.judul else "", color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(modifier=Modifier.heightIn(min=50.dp),lineHeight = 13.sp, maxLines = 3, text = if(data.deskripsi!=null) data.deskripsi else "Tidak ada deskripsi \n\n\n\n", color = MaterialTheme.colorScheme.outline, fontSize = 12.sp)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)) {
                        Icon(modifier= Modifier
                            .height(50.dp)
                            .padding(end = 10.dp)
                            .clickable(onClick = onBookMark),imageVector = if(data.bookmarked)Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription =null , tint = if(data.bookmarked) Color.Red else MaterialTheme.colorScheme.outline)
                        Icon(modifier= Modifier
                            .height(50.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_SEND)
                                val url = data.url

                                intent.type = "text/html"
                                intent.putExtra(Intent.EXTRA_SUBJECT, "sample")
                                intent.putExtra(Intent.EXTRA_TEXT, url)

                                context.startActivity(Intent.createChooser(intent, "Share"))
                            }
                            ,imageVector = Icons.Default.Share, contentDescription =null , tint=MaterialTheme.colorScheme.outline)
                    }
                }
                Column(modifier = Modifier
                    .weight(.4f)
                    .fillMaxSize()) {
                    GlideImage(
                        model = if(data.gambar!=null)data.gambar else R.drawable.berita_banner,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = 0.99f }
                            .drawWithContent {
                                val colors = listOf(
                                    Color.Black,
                                    Color.Transparent
                                )
                                drawContent()
                                drawRect(
                                    blendMode = BlendMode.DstOut,
                                    brush = Brush.horizontalGradient(colors),
                                )
                            }
                    ){it.centerCrop()}
                }
            }
        }

    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun BeritaDetailView(onClickAction: () -> Unit, dataBerita:MutableState<beritaModel>, onBookMark: () -> Unit = {}){
        ConstraintLayout(modifier = Modifier
            //.clickable(true, onClick = {})
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()) {
            val (image,button,deskripsi,bg) = createRefs()

            //Gambar berita
            Column(modifier = Modifier
                .fillMaxSize()
                .constrainAs(image) {
                    top.linkTo(parent.top); bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start);end.linkTo(parent.end)
                }) {


                GlideImage(modifier= Modifier
                    .fillMaxSize()
                    .weight(.5f),
                    model = if(dataBerita.value.gambar!=null)dataBerita.value.gambar else R.drawable.berita_banner,
                    contentDescription = null){
                    it.centerCrop()
                }

                Box(modifier = Modifier
                    .clip(RoundedCornerShape(50, 0, 0, 0))
                    .background(Color.Transparent)
                    .weight(.5f)
                    .fillMaxSize())
            }

            //des
            Column(modifier = Modifier
                .fillMaxSize()
                .constrainAs(bg) {
                    top.linkTo(parent.top); bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start);end.linkTo(parent.end)
                }) {
                Column(modifier = Modifier
                    .weight(.1f)
                    .fillMaxSize()
                    .background(Color.Transparent)){}
                Image(contentScale = ContentScale.FillBounds, modifier = Modifier
                    .weight(.9f)
                    .fillMaxSize(),painter = painterResource(id = R.drawable.design), contentDescription =null )
            }

            Column(Modifier.constrainAs(deskripsi){
                top.linkTo(parent.top); start.linkTo(parent.start); bottom.linkTo(parent.bottom); end.linkTo(parent.end)
            }) {
                Box(
                    Modifier
                        .weight(.31f)
                        .fillMaxSize())
                Column(
                    Modifier
                        .clip(RoundedCornerShape(38, 0, 0, 0))
                        .verticalScroll(rememberScrollState())
                        .weight(.69f)
                        .padding(horizontal = 25.dp)) {
                    Text(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top = 30.dp),text = dataBerita.value.waktu,textAlign = TextAlign.End, color=Color.Yellow)
                    Text(modifier = Modifier.padding(bottom=15.dp),maxLines = 3,text = dataBerita.value.judul, color=Color.White,fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 27.sp)
                    PairText(modifier = Modifier.padding(bottom=5.dp),param1 ="Kategori ", param2 =  if(dataBerita.value.kategori!=null)dataBerita.value.kategori else "-",.3f , fontSize = 16.sp)
                    PairText(modifier = Modifier.padding(bottom=5.dp),param1 = "Author ", param2 = dataBerita.value.author,.3f , fontSize = 16.sp)
                    PairText(modifier = Modifier.padding(bottom=20.dp),param1 = "Sumber ", param2 = dataBerita.value.sumber,.3f , fontSize = 16.sp)

                    Text(modifier= Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
                        color=Color.White,
                        text = if(dataBerita.value.deskripsi!=null)dataBerita.value.deskripsi else "Tidak ada deskripsi !",
                        fontSize = 16.sp, textAlign = TextAlign.Justify)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                    ) {
                        Column(Modifier.weight(.7f)) {
                            Button(modifier=Modifier.fillMaxWidth(),colors = ButtonDefaults.buttonColors(Color.Yellow)
                                ,onClick = {
                                    var url = dataBerita.value.url
                                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                        url = "http://$url"
                                    }

                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    startActivity(context,browserIntent,null)
                                }
                            ) {
                                Text(text = "Kunjungi sumber berita", color = colorPalettes().darkBlues)
                            }
                        }
                        Column(Modifier.weight(.15f)) {
                            Icon(modifier= Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .clickable(onClick = onBookMark),imageVector = if(dataBerita.value.bookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder , contentDescription = null, tint = if(dataBerita.value.bookmarked)Color.Red else Color.White)
                        }
                        Column(Modifier.weight(.15f)) {
                            Icon(modifier= Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .clickable {
                                    val intent = Intent(Intent.ACTION_SEND)
                                    val url = dataBerita.value.url

                                    intent.type = "text/html"
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "sample")
                                    intent.putExtra(Intent.EXTRA_TEXT, url)

                                    context.startActivity(Intent.createChooser(intent, "Share"))
                                },imageVector = Icons.Default.Share , contentDescription = null, tint = Color.White )
                        }

                    }
                    //Blank
                    Box(
                        Modifier
                            .height(150.dp)
                            .fillMaxWidth())
                }
            }


            //Close button
            Box(modifier = Modifier
                .clickable { }
                .constrainAs(button) {
                    top.linkTo(parent.top, 20.dp); end.linkTo(parent.end, 20.dp)
                }
                .clip(CircleShape)
            ){
                Icon(
                    tint = Color.White,
                    imageVector = Icons.Default.Clear ,
                    contentDescription = null ,
                    modifier = Modifier
                        .size(32.dp)
                        .background(colorPalettes().dark25)
                        .clickable(onClick = onClickAction)
                )
            }
            
            //Aksi are


        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MySearchBar(color:Color = MaterialTheme.colorScheme.primaryContainer, modifiers:Modifier, actions: () -> Unit, cancel: () -> Unit, keyword: MutableState<String>){
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = {-50}),
            exit = slideOutVertically(targetOffsetY = {-50})
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (bar, btn) = createRefs()

                MyTextFields(
                    fontColor = MaterialTheme.colorScheme.outline,
                    value = keyword.value,
                    onValueChange = {
                        keyword.value = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        selectionColors = TextSelectionColors(
                            handleColor = Color.White,
                            backgroundColor = Color.White
                        ),
                        disabledTextColor = colorPalettes().Invisible,
                        focusedIndicatorColor = colorPalettes().LightBlue,
                        focusedTrailingIconColor = Color.White,
                        unfocusedIndicatorColor = colorPalettes().Invisible,
                        disabledIndicatorColor = colorPalettes().Invisible,
                        containerColor = color,
                        textColor = Color.Blue,
                        placeholderColor = Color.White
                    ),
                    shape = RoundedCornerShape(50, 50, 50, 50),
                    modifier = modifiers.constrainAs(bar) {
                        top.linkTo(parent.top);end.linkTo(parent.end);start.linkTo(parent.start);bottom.linkTo(
                        parent.bottom
                    )
                    },
                    leadingIcon = {
                        Icon(
                            tint = MaterialTheme.colorScheme.outline,
                            painter = painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = "Password"
                        )
                    },
                    placeholder = {
                        PlaceHolder("Cari berita")
                    },
                    keywordOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            actions()
                        }
                    )
                )

                IconButton(
                    modifier = Modifier.constrainAs(btn) {
                        end.linkTo(parent.end,5.dp);top.linkTo(parent.top);bottom.linkTo(parent.bottom)
                    },
                    onClick =cancel

                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                }

            }
        }

    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyTextFields(
        maxLines:Int=1,
        fontColor:Color=Color.Black,
        leadingIcon:  @Composable (() -> Unit)? = null,
        trailingIcon:  @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        value:String = "",
        modifier:Modifier,
        onValueChange: (String) -> Unit,
        colors:androidx.compose.material3.TextFieldColors = TextFieldDefaults.textFieldColors(MaterialTheme.colorScheme.outline),
        shape: Shape = TextFieldDefaults.filledShape,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        keywordOptions: KeyboardOptions = KeyboardOptions.Default,
        passwordVisible:MutableState<Boolean> = mutableStateOf(false)
    ){
        BasicTextField(
            value = value,
            modifier = modifier
                .defaultMinSize(
                    minHeight = 14.dp
                )
                .fillMaxWidth()
                .padding(all = 0.dp),
            onValueChange = onValueChange,
            enabled = true,
            readOnly = false,
            cursorBrush = SolidColor(colorPalettes().LightBlue),
            visualTransformation = if (!passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = keywordOptions,
            keyboardActions = keyboardActions,
            interactionSource = remember { MutableInteractionSource() },
            singleLine = true,
            maxLines = maxLines,
            textStyle = androidx.compose.ui.text.TextStyle(color=fontColor),
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
                    shape = shape,
                    singleLine = true,
                    enabled = true,
                    isError = false,
                    interactionSource = remember { MutableInteractionSource() },
                    colors = colors
                )
            }

        )
    }


    @Composable
    fun PairText(modifier:Modifier = Modifier, param1:String, param2:String, fweight:Float=.3f, fontSize:TextUnit=14.sp){
        Row(modifier = modifier) {
            Column(Modifier.weight(fweight)) {
                Text(text = param1,fontSize = fontSize, color = Color.White)
            }
            Column {
                Text(": ",fontSize = fontSize, color = Color.White)
            }
            Column(Modifier.weight(1f-fweight)) {
                Text(text = param2,fontSize = fontSize, color = Color.White)
            }
        }
    }



    @Composable
    fun myDialog(
        dialog : DialogModel = DialogModel(Color.White,R.drawable.baseline_check_circle_outline_24,Color.Yellow,"Apakah anda yakin ?","Harap konfirmasi !"),
        buttons:ArrayList<ButtonModel>
    ){
        Box(
            modifier = dialog.modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .clip(RoundedCornerShape(10))
        ) {
            Column(
                modifier = Modifier
                    .background(dialog.bgColor)
                    .padding(vertical = 20.dp ,horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Icon(modifier = Modifier.padding(bottom = 20.dp),painter = painterResource(id = dialog.drawID),contentDescription = null, tint = dialog.tint)
                Text(modifier = Modifier.padding(bottom = 5.dp), fontWeight = FontWeight.Bold, text=dialog.label,fontSize = 16.sp, textAlign = TextAlign.Center,color = Color.Black)
                Text(modifier = Modifier.padding(bottom = 10.dp),text=dialog.text,fontSize = 14.sp,color = Color.Black, textAlign = TextAlign.Center)
                Row(horizontalArrangement = Arrangement.Center){
                    buttons.forEach{
                        Button(modifier = it.modifier.padding(horizontal = 5.dp),onClick = it.action, colors = ButtonDefaults.buttonColors(it.bgColor)){
                            Text(text=it.label, color = it.txtColor)
                        }
                    }

                }
            }
        }

    }

    class ButtonModel(action:()->Unit,bg:Color,txt:Color, label:String, mod:Modifier = Modifier){
        val action:() -> Unit = action
        val bgColor:Color = bg
        val label:String = label
        val txtColor :Color = txt
        val modifier:Modifier = mod
    }

    class DialogModel(bg: Color,drawID: Int,tint:Color,label: String,text: String,mod: Modifier=Modifier){
        val drawID = drawID
        val label = label
        val text = text
        val bgColor = bg
        val tint:Color = tint
        val modifier = mod
    }

}