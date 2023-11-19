package com.example.myapplication.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoadingScreen(navController: NavController, encodedUri: String) {
    val encodedUri1 = URLEncoder.encode(encodedUri, "UTF-8")
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectedUri = URLDecoder.decode(encodedUri, "UTF-8")

    var isToggled by remember { mutableStateOf(false) }
    val toggleImage: Painter = if (isToggled) {
        painterResource(R.drawable.toggle_on)
    } else {
        painterResource(R.drawable.toggle_off)
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
                            painter = toggleImage,
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
                // 모델 예측
                LoadingPrediction(context, selectedUri, coroutineScope, navController, encodedUri1)

                // 빈 카드 리스트 넣을 공간
                Text(text = "로딩중❤️❤️로딩중❤️❤️로딩중❤️❤️로딩중❤️❤️로딩중❤️❤️로딩중❤️❤️로딩중❤️❤️")

            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun LoadingPrediction(
    context: Context,
    selectedUri: String?,
    coroutineScope: CoroutineScope,
    navController: NavController,
    encodedUri1: String?
) {
    suspend fun uploadImage(imageUri: Uri): String? = withContext(Dispatchers.IO) {
        val url = "http://192.168.35.177:5000/prediction"
        val client = OkHttpClient()

        val inputStream = context.contentResolver.openInputStream(imageUri)
        val file = createFileFromInputStream(inputStream)

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "img",
                "img.jpg",
                RequestBody.create("img/*".toMediaTypeOrNull(), file)
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        var prediction: String? = null

        try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody)
                prediction = jsonObject.getString("prediction")
                Log.d("성공함", "이미지가 올라갔다? Respones : ${responseBody ?: "no data"}")
            } else {
                Log.e("망함", "망함")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return@withContext prediction
    }

    if (selectedUri != null) {
        coroutineScope.launch {
            val predictValue = uploadImage(Uri.parse(selectedUri))
            if (predictValue != null) {
                navController.navigate(
                    "output/${predictValue}/${encodedUri1}"
                )
            }
        }
    }
}


// 데이터 리스트로 전달할 때
//                var dataList = mutableListOf(predictValue, selectedUri)
//                dataList.add(predictValue)
//                dataList.add(selectedUri)