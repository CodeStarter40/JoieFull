package com.openclassrooms.joiefull

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.joiefull.theme.JoieFullTheme
import dagger.hilt.android.AndroidEntryPoint

import com.openclassrooms.joiefull.ui.home.HomeActivity
import com.openclassrooms.joiefull.ui.wanstatus.NoWanActivity
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoieFullTheme {
                SplashScreen()
                LaunchedEffect(true) {
                    delay(2000)
                    if (!isInternetAvailable(this@MainActivity)) {
                        startActivity(Intent(this@MainActivity, NoWanActivity::class.java))
                        finish()
                        return@LaunchedEffect
                    } else {
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        finish()
                }
            }
        }
    }
}
//verify internet connexion
private fun isInternetAvailable(context: Context):Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.orange)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    JoieFullTheme {
        SplashScreen()
    }
}
}