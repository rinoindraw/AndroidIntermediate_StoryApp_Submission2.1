package com.rinoindraw.storybismillah.database.story

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rinoindraw.storybismillah.database.model.Story

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(vararg story: Story)

    @Query("SELECT * FROM tbl_story")
    fun getAllStories(): PagingSource<Int, Story>

    @Query("DELETE FROM tbl_story")
    suspend fun deleteAllStories()

}