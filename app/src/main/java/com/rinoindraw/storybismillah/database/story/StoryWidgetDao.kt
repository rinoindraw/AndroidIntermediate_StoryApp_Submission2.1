package com.rinoindraw.storybismillah.database.story

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rinoindraw.storybismillah.database.model.Story

@Dao
interface StoryWidgetDao {

    @Query("SELECT * FROM tbl_story")
    fun getAllStoriesWidget(): List<Story>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(storyList:  List<Story>)

    @Query("DELETE FROM tbl_story")
    suspend fun deleteAllStories()

}