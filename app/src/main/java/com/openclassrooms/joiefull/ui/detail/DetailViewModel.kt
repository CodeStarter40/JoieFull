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


    //stockage nb de like
    private val _likesCount = MutableStateFlow(0)
    val likesCount: StateFlow<Int> = _likesCount

    //stockage de l'état du like
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked

    fun fetchItem(itemId: Int) {
        viewModelScope.launch {

            val allItems = repository.getItems()
            allItems.collect { itemsList ->
                val item = itemsList.find { it.id == itemId }
                _item.value = item
                val foundItem = itemsList.find { it.id == itemId }
                foundItem?.let {
                    _likesCount.value = it.likes
                }
            }
        }
    }

    fun addLike() {
        viewModelScope.launch {
            val currentLikedStatus = _isLiked.value
            if (currentLikedStatus) {
                _likesCount.value--
            } else {
                _likesCount.value++

            }
            _isLiked.value = !currentLikedStatus
        }
    }
}