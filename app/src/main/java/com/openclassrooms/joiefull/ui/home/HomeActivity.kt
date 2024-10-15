package com.openclassrooms.joiefull.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.openclassrooms.joiefull.data.api.ClothesItem
import com.openclassrooms.joiefull.theme.JoieFullTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.ui.detail.DetailActivity

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoieFullTheme {
                val viewModel: HomeViewModel = hiltViewModel()
                val items = viewModel.items.collectAsState().value
                val configuration = LocalConfiguration.current
                val isTablet = configuration.screenWidthDp >= 800
                val selectedItem = remember { mutableStateOf<ClothesItem?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    if (isTablet) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            //liste des items sur la gauche
                            ItemsByCategoryScreen(
                                items = items,
                                modifier = Modifier.weight(2f),
                                onItemClick = { item ->
                                    selectedItem.value = item
                                }
                            )

                            //volet de droite pour afficher les details
                            if (selectedItem.value != null) {
                                DetailScreenTablet(selectedItem.value!!, modifier = Modifier.weight(1f))
                            } else {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Touchez un article pour afficher les détails",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    } else {
                        //version smarrtphone lance DetailActivity
                        ItemsByCategoryScreen(items = items, onItemClick = { item ->
                            val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                            intent.putExtra("item", item.id)
                            startActivity(intent)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun ItemsByCategoryScreen(items: List<ClothesItem>, modifier: Modifier = Modifier, onItemClick: (ClothesItem) -> Unit) {
    //groupement des items par catégorie
    val groupedItems = items.groupBy { it.category }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 35.dp)
    ) {
        groupedItems.forEach { (category, itemsInCategory) ->
            //affichage de chaque caté
            item {
                Text(
                    text = category,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(start = 8.dp)
                        .fillMaxWidth(),
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(itemsInCategory) { item ->
                        ItemRow(item = item, onItemClick = onItemClick)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemRow(item: ClothesItem, onItemClick: (ClothesItem) -> Unit) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onItemClick(item) }
    ) {
        Box(modifier = Modifier
            .size(220.dp)
            .clip(RoundedCornerShape(25.dp))
        ) {
            Image(
                painter = rememberImagePainter(data = item.picture.url),
                contentDescription = item.picture.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize())

            //like button
            Button(onClick = { /*TODO*/ }, //imp logique pour le like
                colors = androidx.compose.material3.ButtonDefaults.buttonColors( Color.White),
                contentPadding = PaddingValues(7.dp),
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
                        .padding(end = 5.dp)

                )
                Text(text = item.likes.toString(), color = Color.Black, fontSize = 18.sp)
            }
        }
        //nom de l'article
        Text(
            text = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .padding(top = 2.dp)
                .align(Alignment.End)
        ) {
            //prix de l'article
            Text(
                text = "${item.price}€",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 120.dp),
            )
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
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 4.dp)
            )
        }
        //original price
        Text(
            text = "${item.original_price}€",
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Start),
            style = TextStyle(
                color = Color.Black,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreenTablet(item: ClothesItem, modifier: Modifier) {
        //tout dans une box
        Box(
            modifier = modifier
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
                            .height(500.dp)
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
                            .padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name,
                            modifier = Modifier
                                .padding(top = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier
                            .weight(1f))
                        //rating part
                        Image(
                            painter = painterResource(id = R.drawable.star_yellow),
                            contentDescription = "Rating Star",
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Text(
                            text = "4.3",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp
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
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${item.original_price}€",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 18.sp,
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
                        text = item.picture.description,
                        modifier = Modifier
                            .padding(top = 8.dp)
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
                        label = { Text("Partagez ici vos impressions sur ce produit",color = Color.Gray) },
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


@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    val density = LocalDensity.current.density
    val starSize = (15f * density).dp
    val starSpacing = (0.8f * density).dp

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star
            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color(0xFFA0A0A0)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onRatingChanged(i.toFloat())
                        }
                    )
                    .width(starSize)
                    .height(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}
