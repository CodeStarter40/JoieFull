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

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            //collecter les données du Flow renvoyé par le repository
            repository.getItems().collect { itemList -> _items.value = itemList
                Log.d("HomeViewModel", "ItemsCount = ${itemList.size}")
            }
        }
    }
}

