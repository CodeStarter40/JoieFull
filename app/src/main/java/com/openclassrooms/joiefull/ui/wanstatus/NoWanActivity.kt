package com.openclassrooms.joiefull.ui.wanstatus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.joiefull.R
import com.openclassrooms.joiefull.theme.JoieFullTheme
import com.openclassrooms.joiefull.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoWanActivity : ComponentActivity() {
    private val viewModel: NoWanViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoieFullTheme {
                val isInternetAvailable by viewModel.isInternetAvailable.collectAsState()

                LaunchedEffect(isInternetAvailable) {
                    if (isInternetAvailable) {
                        startActivity(Intent(this@NoWanActivity, HomeActivity::class.java))
                        finish()
                    }
                }
                Box(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.orange))
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cloudy),
                            contentDescription = "sad cloud",
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .size(150.dp)
                        )
                        Text(
                            text = "Pas de connexion Internet",
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}
