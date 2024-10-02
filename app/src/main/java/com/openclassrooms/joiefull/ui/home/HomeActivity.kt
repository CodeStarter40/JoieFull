package com.openclassrooms.joiefull.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.openclassrooms.joiefull.data.api.ClothesItem
import com.openclassrooms.joiefull.theme.JoieFullTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoieFullTheme {
                //utilisation du viewmodel
                val viewModel: HomeViewModel = hiltViewModel()
                val items = viewModel.items.collectAsState().value

                //affichage de la liste
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    ItemsListScreen(items = items, onItemClick = { item ->
                        //nav vers detail
                    })
                }
            }
        }
    }
}
@Composable
fun ItemsListScreen(items: List<ClothesItem>, onItemClick: (ClothesItem) -> Unit) {
    LazyColumn {
        items(items) { item ->
            ItemRow(item = item, onItemClick = onItemClick)
        }
    }
}
@Composable
fun ItemRow(item: ClothesItem, onItemClick: (ClothesItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .padding(8.dp)
    ) {
        //test image coil
        Image(
            painter = rememberImagePainter(data = item.picture.url),
            contentDescription = item.picture.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(128.dp)
                .padding(end = 8.dp)
                .clip(shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
        )

        //affichage du nom de l'item
        Text(
            text = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

