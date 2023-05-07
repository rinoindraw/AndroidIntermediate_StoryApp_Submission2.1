package com.rinoindraw.storybismillah.database.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rinoindraw.storybismillah.database.model.AddStoriesResponse
import com.rinoindraw.storybismillah.database.model.GetStoriesResponse
import com.rinoindraw.storybismillah.database.model.Story
import com.rinoindraw.storybismillah.database.remotekeys.StoryRemoteMediator
import com.rinoindraw.storybismillah.database.story.StoryAppDatabase
import com.rinoindraw.storybismillah.database.story.StoryService
import com.rinoindraw.storybismillah.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
class StoryRepository @Inject constructor(
    private val storyDatabase: StoryAppDatabase,
    private val storyService: StoryService,
) {

    fun getAllStories(token: String): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                storyService,
                initializeBearerToken(token)
            ),
            pagingSourceFactory = {
                storyDatabase.getstoryDao().getAllStories()
            }
        ).flow
    }

    suspend fun addNewStories(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Flow<Result<AddStoriesResponse>> = flow {
        try {
            val bearerToken = initializeBearerToken(token)
            val storyResponse = storyService.addNewStories(bearerToken, file, description, lat, lon)
            emit(Result.success(storyResponse))
        } catch (story: Exception) {
            story.printStackTrace()
            emit(Result.failure(story))
        }
    }

    fun getStoriesLocation(token: String): Flow<Result<GetStoriesResponse>> = flow {
        wrapEspressoIdlingResource {
            try {
                val bearerToken = initializeBearerToken(token)
                val storyResponse = storyService.getAllStories(bearerToken, size = 30, location = 1)
                emit(Result.success(storyResponse))
            } catch (story: Exception) {
                story.printStackTrace()
                emit(Result.failure(story))
            }
        }
    }

    private fun initializeBearerToken(token: String): String {
        return "Bearer $token"
    }

}