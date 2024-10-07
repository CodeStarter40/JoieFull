package com.openclassrooms.joiefull.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
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

                Scaffold(modifier = Modifier.fillMaxSize()) {
                        ItemsByCategoryScreen(items = items, onItemClick = { item -> //nav vers detail item
                        val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                        intent.putExtra("item", item.id)
                        startActivity(intent) //passer l'ID de l'article
                        })
                    }
                }
            }
        }
    }


@Composable
fun ItemsByCategoryScreen(items: List<ClothesItem>, onItemClick: (ClothesItem) -> Unit) {
    //groupement des items par caté
    val groupedItems = items.groupBy { it.category }

    //affichage des items par catégorie
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(top = 25.dp)) {
        groupedItems.forEach { (category, itemsInCategory) ->
            //encapsulage
            item {
                //titre de la catégorie
                Text(
                    text = category,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(start = 8.dp)
                        .fillMaxWidth(),
                )

                //affichage des items DE LA CATEGO
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
            Button(onClick = { /*TODO*/ },
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
        //prix de l'article
        Text(
            text = "${item.price}€",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
        )
    }
}
