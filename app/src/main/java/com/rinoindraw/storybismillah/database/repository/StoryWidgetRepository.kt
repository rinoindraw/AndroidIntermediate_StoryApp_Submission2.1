package com.rinoindraw.storybismillah.database.repository

import com.rinoindraw.storybismillah.database.ApiResponse
import com.rinoindraw.storybismillah.database.model.AddStoriesResponse
import com.rinoindraw.storybismillah.database.model.GetStoriesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryWidgetRepository @Inject constructor(private val storyDataSource: StoryWidgetRepository) {

    suspend fun getAllStoriesWidget(token: String, limit: Int): Flow<ApiResponse<GetStoriesResponse>> {
        return storyDataSource.getAllStoriesWidget(token, limit).flowOn(Dispatchers.IO)
    }

    suspend fun addNewStories(token: String, file: MultipartBody.Part, description: RequestBody): Flow<ApiResponse<AddStoriesResponse>> {
        return storyDataSource.addNewStories(token, file, description)
    }

}