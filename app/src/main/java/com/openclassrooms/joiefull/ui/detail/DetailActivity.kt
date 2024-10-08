package com.openclassrooms.joiefull.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.data.api.ClothesItem
import com.openclassrooms.joiefull.theme.JoieFullTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val itemId = intent.getIntExtra("item", -1)

        setContent {
            JoieFullTheme {
                val detailViewModel: DetailViewModel = hiltViewModel()

                LaunchedEffect(itemId) {
                    detailViewModel.fetchItem(itemId)
                }

                val item by detailViewModel.item.collectAsState()

                item?.let {
                    DetailScreen(it)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(item: ClothesItem) {
    Scaffold {
        //tout dans une box
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    //box qui contient l'image et les boutons retour, like et share
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp)
                            .padding(bottom = 15.dp),
                        contentAlignment = Alignment.Center //centre l'image
                    ) {
                        Image(
                            painter = rememberImagePainter(data = item.picture.url),
                            contentDescription = item.picture.description,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .height(380.dp)
                                .clip(RoundedCornerShape(25.dp))
                        )
                        BackArrowButton() //fleche de retour
                        IconButton(
                            onClick = { /*TODO*/ }, //imp logique de click
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "Share"
                            )
                        }
                        //like button
                        Button(
                            onClick = { /*TODO*/ }, //imp logique de click add like unlike
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color.White),
                            contentPadding = PaddingValues(8.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 10.dp, end = 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_like_empty),
                                contentDescription = "Like",
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(end = 6.dp)
                            )
                            Text(
                                text = item.likes.toString(),
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                //name and rating part
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp)
                    ) {
                        Text(
                            text = item.name,
                            modifier = Modifier.padding(top = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        //rating part
                        Image(
                            painter = painterResource(id = R.drawable.star_yellow),
                            contentDescription = "Rating Star",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "4.3",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                //price et original price part
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.price}€",
                            modifier = Modifier.padding(top = 4.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${item.original_price}€",
                            style = TextStyle(
                                color = Color.Gray,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                //description of picture product
                item {
                    Text(
                        text = item.picture.description,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                //implementation profil_picture, and rating selection
                item {

                }
            }
        }
    }
}
//arrow back button with click on it logic
@Composable
fun BackArrowButton() {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = { if (context is DetailActivity) context.finish() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Black
            )
        }
    }
}