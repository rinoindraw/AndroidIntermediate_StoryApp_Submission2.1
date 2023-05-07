package com.rinoindraw.storybismillah.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rinoindraw.storybismillah.database.model.Story

class PagingTestingData : PagingSource<Int, Story>() {

    companion object {
        fun snap(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val items = emptyList<Story>()
        return LoadResult.Page(items, null, null)
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return null
    }
}