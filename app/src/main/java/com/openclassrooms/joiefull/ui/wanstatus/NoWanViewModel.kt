package com.openclassrooms.joiefull.ui.wanstatus

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoWanViewModel @Inject constructor(application: Application):AndroidViewModel(application){
    private val _isInternetAvailable = MutableStateFlow(false)
    val isInternetAvailable: MutableStateFlow<Boolean> = _isInternetAvailable

    init {
        checkNetworkAvailabilityLoop()
    }

    private fun checkNetworkAvailabilityLoop() {
        viewModelScope.launch {
            while (true) {
                _isInternetAvailable.value = isInternetAvailable(getApplication())
                delay(1500)
            }
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}