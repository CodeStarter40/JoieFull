package com.openclassrooms.joiefull

import com.openclassrooms.joiefull.data.api.ClothesApiService
import com.openclassrooms.joiefull.data.api.ClothesItem
import com.openclassrooms.joiefull.data.api.Picture
import com.openclassrooms.joiefull.data.repository.ClothesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class ClothesRepositoryTest {

    @Test
    fun getItemsReturnListItems() = runBlocking {
        //mock the API response
        val mockApiService = Mockito.mock(ClothesApiService::class.java)
        val mockItems = listOf(
            ClothesItem(
                id = 1,
                picture = Picture(url = "https://test.fr/image1.jpg", description = "Description 1"),
                name = "T-shirt",
                price = 19.99,
                quantity = 10,
                original_price = 29.99,
                likes = 5,
                category = "Hauts"
            )
        )

        //setup the mock behavior to return the mockitems list
        Mockito.`when`(mockApiService.getItems()).thenReturn(mockItems)

        //create an instance of clothesrepository with the mocked API service
        val repository = ClothesRepository(mockApiService)

        //call the method under test
        val result = repository.getItems().first()

        //verify the expected output
        assertEquals(mockItems, result)
    }
}

