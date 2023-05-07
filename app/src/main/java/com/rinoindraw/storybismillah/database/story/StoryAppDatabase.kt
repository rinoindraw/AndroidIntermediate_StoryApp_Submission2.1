package com.rinoindraw.storybismillah.database.story

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rinoindraw.storybismillah.database.remotekeys.RemoteKeys
import com.rinoindraw.storybismillah.database.model.Story
import com.rinoindraw.storybismillah.database.remotekeys.RemoteKeysDao

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryAppDatabase : RoomDatabase() {
    abstract fun getstoryDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun getStoryDaoWidget(): StoryWidgetDao
}