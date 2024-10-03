package com.openclassrooms.joiefull.ui.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoieFullTheme {
                val viewModel: HomeViewModel = hiltViewModel()
                val items = viewModel.items.collectAsState().value

                Scaffold(modifier = Modifier.fillMaxSize()) {
                        ItemsByCategoryScreen(items = items, onItemClick = { item -> //nav vers detail item à impl
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
    Column(modifier = Modifier.padding(8.dp)) {
        groupedItems.forEach { (category, itemsInCategory) ->
            //titre de la catégorie
            Text(
                text = category,
                modifier = Modifier
                    .padding(8.dp)
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

@Composable
fun ItemRow(item: ClothesItem, onItemClick: (ClothesItem) -> Unit) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onItemClick(item) }
    ) {
        //coil test
        Image(
            painter = rememberImagePainter(data = item.picture.url),
            contentDescription = item.picture.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(25.dp))
        )
        //nom de l'article
        Text(
            text = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
        //prix de l'article
        Text(
            text = item.price.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}
