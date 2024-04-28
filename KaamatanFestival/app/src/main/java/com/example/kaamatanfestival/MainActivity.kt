package com.example.kaamatanfestival

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kaamatanfestival.ui.theme.KaamatanFestivalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaamatanFestivalTheme {
                KaamatanFestival()
            }
        }
    }
}

@Preview (showSystemUi = true, showBackground = true)
@Composable
fun KaamatanFestival(){
    LayOut(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center),
    )
}

class ImageText(private val image: Painter, private val text: String){
    @Composable
    fun Draw(){
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = text,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun LayOut(modifier: Modifier = Modifier){
    val imageList = listOf(
        ImageText(painterResource(R.drawable.selamat_hari_kaamatan), "Selamat Menyambut Hari Kaamatan"),
        ImageText(painterResource(R.drawable.pakaian), "Happy Harvest Festival"),
        ImageText(painterResource(R.drawable.tarian_sumazau), "Kotobian Tadau Tagazo Do Kaamatan"),
        ImageText(painterResource(R.drawable.sambutan_kaamatan), "丰收节快乐"),
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var currentIndex by remember { mutableIntStateOf(0)}
        val current = imageList[currentIndex]
        current.Draw()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {currentIndex = if (currentIndex - 1 < 0) imageList.size - 1 else currentIndex - 1}) {
                Text(text = "Previous")
            }
            Button(onClick = {currentIndex = (currentIndex + 1) % imageList.size}) {
                Text(text = "Next")
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Text(text = "Design by Sirajuddin")
    }
}