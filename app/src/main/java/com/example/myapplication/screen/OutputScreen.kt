package com.example.myapplication.Screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Component.uriToBitmap
import com.example.myapplication.R
import com.example.myapplication.database.getFoodNutrientByName
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputActivity(navController: NavController, predictValue: String, selectedUri: String) {
    var isToggled by remember { mutableStateOf(false) }

    val toggleImage: Int = if (isToggled) {
        R.drawable.toggle_on
    } else {
        R.drawable.toggle_off
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "홍길동",
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "프로필 사진"
                        )
                    }
                },
                actionIcon = {
                    IconButton(
                        onClick = { isToggled = !isToggled }
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = toggleImage),
                            contentDescription = "토글 아이콘"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                BottomBar(
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.navigate("calender") },
                            modifier = Modifier
                                .size(width = 80.dp, height = 78.dp)
                                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.calender),
                                contentDescription = "내 기록 아이콘 (캘린더)",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    },
                    actionIcon1 = {
                        IconButton(
                            onClick = { navController.navigate("input") },
                            modifier = Modifier
                                .size(width = 80.dp, height = 78.dp)
                                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.info),
                                contentDescription = "영양정보 아이콘",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    },
                    actionIcon2 = {
                        IconButton(
                            onClick = { /* doSomething() */},
                            modifier = Modifier
                                .size(width = 80.dp, height = 78.dp)
                                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.consult),
                                contentDescription = "상담 아이콘",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    },
                    actionIcon3 = {
                        IconButton(
                            onClick = { /* doSomething() */},
                            modifier = Modifier
                                .size(width = 80.dp, height = 78.dp)
                                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.state),
                                contentDescription = "내 상태 아이콘",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                )
            }
        },
        content = { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                OutputScreen(navController, predictValue, selectedUri)
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OutputScreen(navController: NavController, predictValue: String, selectedUri: String) {
    val foodNutrient = getFoodNutrientByName(predictValue)
    val context = LocalContext.current
    val bitmap: Bitmap? = Uri.parse(selectedUri)?.let { uriToBitmap(it, context) }
    val resources = context.resources
    val defaultImageBitmap =
        BitmapFactory.decodeResource(resources, R.drawable.no_image).asImageBitmap()

    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        // 뒤로 가기
        IconButton(
            onClick = {
                navController.popBackStack()
                navController.popBackStack()

            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.goback),
                contentDescription = "Go back",
            )
        }
    }
    // 상단 여백
    Spacer(modifier = Modifier.height(40.dp))
    // 해당 음식 이미지
    Image(
        // painter = painterResource(id = R.drawable.pizza),
        bitmap = bitmap?.asImageBitmap() ?: defaultImageBitmap,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(300.dp)
            .border(width = 3.dp, Color.Gray)
    )
    // 이미지와 이름 여백
    Spacer(modifier = Modifier.height(15.dp))
    // 음식 이름 Text
    foodNutrient?.let {
        FoodInfo(redText = it.menu, blackText = "입니다")
    }
    // 정렬을 위한 Column
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.width(300.dp)
    ) {
        // 음식 성분 정도 Text
        foodNutrient?.let {
            NutrientInfo(
                explainText = "열량 : ",
                quantityText = it.kcalPer100g,
                unitText = " kcal (100g 당)"
            )
        }
        foodNutrient?.let {
            NutrientInfo(
                explainText = "${it.foodUnit} : ",
                quantityText = it.kcalPerUnit,
                unitText = " kcal"
            )
        }
        foodNutrient?.let {
            NutrientInfo(
                explainText = "포함 당질 : ",
                quantityText = it.sugarPer100g,
                unitText = " g (100g 당)"
            )
        }
        foodNutrient?.let {
            NutrientInfo(
                explainText = "${it.foodUnit} : ",
                quantityText = it.sugarPerUnit,
                unitText = " g"
            )
        }
    }
    // 위험도 Text 표시
//        FoodInfo(redText = "고위험", blackText = " 식품군입니다.")
    foodNutrient?.let {
        DangerBox(dangerNum = it.danger)
    }
}

@Composable
fun BlackText(text: String) {
    Text(text = text, color = Color.Black, fontSize = 15.sp)
}

@Composable
fun GrayText(text: String) {
    Text(text = text, color = Color.LightGray, fontSize = 15.sp)
}

@Composable
fun RedText(text: String) {
    Text(text = text, color = Color.Red, fontSize = 30.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun FoodInfo(redText: String, blackText: String) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.wrapContentHeight()
    ) {
        RedText(text = redText)
        Spacer(modifier = Modifier.width(6.dp))
        BlackText(text = blackText)
    }
    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun NutrientInfo(explainText: String, quantityText: Double, unitText: String) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .wrapContentHeight()
    ) {
        GrayText(text = explainText)
        RedText(text = quantityText.toString())
        GrayText(text = unitText)
    }
    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun DangerBox(dangerNum: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 5 downTo 1) {
            Text(
                text = i.toString(),
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .size(45.dp)
                    .background(
                        color = if (dangerNum == i) {
                            when (dangerNum) {
                                1 -> Color.Green
                                2 -> Color.Blue
                                3 -> Color.Yellow
                                4 -> Color.Magenta
                                5 -> Color.Red
                                else -> Color.Cyan
                            }
                        } else {
                            Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .padding(top = 5.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}

fun createFileFromInputStream(inputStream: InputStream?): File {
    val file = File.createTempFile("temp", null)
    inputStream?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
    return file
}


//@Preview
//@Composable
//fun previewSurfaceOut() {
//    Surface(
//        Modifier.fillMaxSize()
//    ) {
//        OutputScreen()
//    }
//}