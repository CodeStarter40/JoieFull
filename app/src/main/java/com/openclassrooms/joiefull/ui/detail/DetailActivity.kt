package com.openclassrooms.joiefull.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.data.api.ClothesItem
import com.openclassrooms.joiefull.theme.JoieFullTheme
import com.openclassrooms.joiefull.ui.wanstatus.NoWanActivity
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
                val error by detailViewModel.error.collectAsState()
                val isLoading by detailViewModel.isLoading.collectAsState()

                //gestion error redirect to NoWanActivity
                if (error != null) {
                    LaunchedEffect(Unit) {
                        startActivity(Intent(this@DetailActivity, NoWanActivity::class.java))
                        finish()
                        Toast.makeText(this@DetailActivity, "Erreur lors du chargement des données : $error", Toast.LENGTH_SHORT).show()
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator() //indcateur de chargement
                    } else {
                        item?.let {
                            DetailScreen(it, detailViewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun DetailScreen(item: ClothesItem, detailViewModel: DetailViewModel) {
    val likesCount by detailViewModel.likesCount.collectAsState()
    val context = LocalContext.current

    Scaffold {
        //tout dans une box
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(top = 35.dp, start = 10.dp, end = 10.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    //box qui contient l'image et les boutons retour, like et share
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(580.dp)
                            .padding(bottom = 15.dp),
                        contentAlignment = Alignment.Center //centre l'image
                    ) {
                        Image(
                            painter = rememberImagePainter(data = item.picture.url),
                            contentDescription = item.picture.description,
                            contentScale = ContentScale.Crop,

                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(25.dp))
                        )
                        BackArrowButton() //fleche de retour
                        //share part
                        IconButton(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Regarde cet article de malade sur JOIEFULL!!! ")
                                    putExtra(Intent.EXTRA_TEXT, "${item.name} au prix de ${item.price}€ sur l'app JOIEFULL")
                                }
                                //launch share chooser
                                context.startActivity(Intent.createChooser(shareIntent, "Partager avec"))
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(48.dp)
                                .padding(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "Share"
                            )
                        }
                        //like button
                        Button(
                            onClick = { detailViewModel.addLike() }, //imp logique de click add like unlike
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color.White),
                            contentPadding = PaddingValues(8.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 10.dp, end = 10.dp)
                        ) {
                            //change le bouton en fonction de l'état du like ( full ou empty )
                            val likeIcon = if (detailViewModel.isLiked.value) {
                                painterResource(id = R.drawable.ic_like_full)
                                } else {
                                painterResource(id = R.drawable.ic_like_empty)
                            }
                            Icon(
                                painter = likeIcon,
                                contentDescription = "Like",
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(28.dp)
                                    .padding(end = 6.dp)
                            )
                            Text(
                                text = likesCount.toString(),
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
                            .padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name,
                            modifier = Modifier
                                .padding(top = 4.dp),
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 23.sp
                            )
                        )
                        Spacer(modifier = Modifier
                            .weight(1f))
                        //rating part
                        Image(
                            painter = painterResource(id = R.drawable.star_black),
                            contentDescription = "Rating Star",
                            modifier = Modifier
                                .size(28.dp)
                        )
                        Text(
                            text = "4.3",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier
                                .padding(start = 4.dp)
                        )
                    }
                }
                //price and original price part
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.price}€",
                            modifier = Modifier
                                .padding(top = 4.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "${item.original_price}€",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            ),
                            modifier = Modifier
                                .padding(top = 4.dp)
                        )
                    }
                }
                //description of picture product
                item {
                    Text(
                        text = item.picture.description + ".",
                        modifier = Modifier
                            .padding(top = 20.dp,bottom = 20.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                }
                //implementation profil_picture, and rating selection
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        var rating by remember { mutableStateOf(0f) }
                        Image(
                            painter = painterResource(id = R.drawable.profil_picture),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop, //recadrage de l'imageé
                            modifier = Modifier
                                .size(50.dp)
                                .padding(top = 8.dp, end = 15.dp)
                                .clip(CircleShape)
                        )
                        //@composable used
                        StarRatingBar(
                            maxStars = 5,
                            rating = rating,
                            onRatingChanged = {
                                rating = it
                            }
                        )
                    }
                }
                //textField for comments rating
                item {
                    var comment by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Partagez ici vos impressions sur ce produit",color = Color.Black) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.Black,

                        )
                    )
                }
            }
        }
    }
}
//arrow back button with click on it logic
@Composable
fun BackArrowButton() {
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()) {
        IconButton(
            onClick = { if (context is DetailActivity) context.finish() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 6.dp)
            )
        }
    }
}
//rating bar @composable
@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    val density = LocalDensity.current.density
    val starSize = 48.dp
    val starSpacing = (0.8f * density).dp

    Row(
        modifier = Modifier
            .selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star
            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color(0xFF808080)
            //description access star rating
            val description = when (i) {
                1 -> "une étoile"
                2 -> "deux étoiles"
                3 -> "trois étoiles"
                4 -> "quatre étoiles"
                5 -> "cinq étoiles"
                else -> "Etoiles"
            }

            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = iconTintColor,
                modifier = Modifier
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onRatingChanged(i.toFloat())
                        }
                    )
                    .width(starSize).height(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}
