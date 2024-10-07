package com.openclassrooms.joiefull.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joiefull.data.api.ClothesItem
import com.openclassrooms.joiefull.data.repository.ClothesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor( private val repository: ClothesRepository) : ViewModel() {
    private val _item = MutableStateFlow<ClothesItem?>(null) //stockage de l'article recup
    val item: StateFlow<ClothesItem?> = _item

    fun fetchItem(itemId: Int) {
        viewModelScope.launch {

            val allItems = repository.getItems()
        allItems.collect { itemsList ->
            val item = itemsList.find { it.id == itemId }
            _item.value = item
            }
        }
    }
}