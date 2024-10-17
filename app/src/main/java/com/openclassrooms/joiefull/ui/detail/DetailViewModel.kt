package com.openclassrooms.joiefull.ui.detail

import android.content.Intent
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

    //stockage de l'état de chargement
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    //stockage de l'état d'erreur
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchItem(itemId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val allItems = repository.getItems()
                allItems.collect { itemsList ->
                    //Dble stockage
                    //stockage des infos globales du produit
                    val item = itemsList.find { it.id == itemId }
                    _item.value = item
                    //variable dediée au stockage du nombre de like only

                    item?.let {
                        _likesCount.value = it.likes
                    }
                }
                } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
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