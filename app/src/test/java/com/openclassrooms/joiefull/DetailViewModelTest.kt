package com.openclassrooms.joiefull

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.joiefull.data.repository.ClothesRepository
import com.openclassrooms.joiefull.ui.detail.DetailViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@ExperimentalCoroutinesApi

class DetailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testAddLike() = runBlocking {
        val mockRepository = Mockito.mock(ClothesRepository::class.java)

        val viewModel = DetailViewModel(mockRepository)

        viewModel.fetchItem(1)

        //0 like isliked is false
        assertEquals(0, viewModel.likesCount.first())
        assertEquals(false, viewModel.isLiked.first())

        viewModel.addLike() //exec addLike

        //1 like isliked is true
        assertEquals(1, viewModel.likesCount.first())
        assertEquals(true, viewModel.isLiked.first())

        viewModel.addLike() //exec addLike

        //0 like isliked is false
        assertEquals(0, viewModel.likesCount.first())
        assertEquals(false, viewModel.isLiked.first())
    }
}