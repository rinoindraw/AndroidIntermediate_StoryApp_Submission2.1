package com.rinoindraw.storybismillah.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rinoindraw.storybismillah.utils.DataDummy
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ListUpdateCallback
import com.rinoindraw.storybismillah.database.model.Story
import com.rinoindraw.storybismillah.database.repository.StoryRepository
import com.rinoindraw.storybismillah.ui.story.StoryAdapter
import com.rinoindraw.storybismillah.utils.CoroutinesTestRules
import com.rinoindraw.storybismillah.utils.PagingTestingData
import com.rinoindraw.storybismillah.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.*


@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRules()

    private lateinit var homeViewModel: HomeViewModel
    private val dummyToken = "authentication_token"

    @Mock
    private lateinit var repository: StoryRepository

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(repository)
    }

    // Skenario Satu
    // Ketika berhasil memuat data cerita.
    // Memastikan data tidak null.
    // Memastikan jumlah data sesuai dengan yang diharapkan.
    // Memastikan data pertama yang dikembalikan sesuai.
    @Test
    fun `Get all stories successfully`() = runBlockingTest {
        val dummyStories = DataDummy.generateDummyListStoriesResponse()
        val data = PagingTestingData.snap(dummyStories)

        val stories = flowOf(data)

        `when`(repository.getAllStories(dummyToken)).thenReturn(stories)

        val actualStories = homeViewModel.getAllStories(dummyToken).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        verify(repository).getAllStories(dummyToken)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories.first(), differ.snapshot().first())
    }

    // Skenario Dua
    // Ketika tidak ada data cerita.
    // Memastikan jumlah data yang dikembalikan nol.
    @Test
    fun `Get all stories with empty response`() = runBlockingTest {
        val dummyStories = emptyList<Story>()
        val data = PagingTestingData.snap(dummyStories)

        val stories = flowOf(data)

        `when`(repository.getAllStories(dummyToken)).thenReturn(stories)

        val actualStories = homeViewModel.getAllStories(dummyToken).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        verify(repository).getAllStories(dummyToken)

        assertNotNull(differ.snapshot())
        assertEquals(0, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}