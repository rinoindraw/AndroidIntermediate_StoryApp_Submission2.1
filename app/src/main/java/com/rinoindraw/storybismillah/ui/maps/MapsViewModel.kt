package com.rinoindraw.storybismillah.ui.maps

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.rinoindraw.storybismillah.database.model.GetStoriesResponse
import com.rinoindraw.storybismillah.database.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MapsViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {

    fun getStoriesLocation(token: String): Flow<Result<GetStoriesResponse>> = storyRepository.getStoriesLocation(token)

}