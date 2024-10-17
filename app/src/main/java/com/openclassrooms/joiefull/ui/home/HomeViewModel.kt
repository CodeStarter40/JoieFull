package com.openclassrooms.joiefull.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joiefull.data.api.ClothesItem
import com.openclassrooms.joiefull.data.repository.ClothesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ClothesRepository) : ViewModel() {

    private val _items = MutableStateFlow<List<ClothesItem>>(emptyList())
    val items: StateFlow<List<ClothesItem>> = _items

    //stockage de l'état de chargement
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    //stockage de l'état d'erreur
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                //collect
                repository.getItems().collect { itemList ->
                    _items.value = itemList
                    Log.d("HomeViewModel", "ItemsCount = ${itemList.size}")
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

