package com.rinoindraw.storybismillah.network

import android.content.Context
import androidx.room.Room
import com.rinoindraw.storybismillah.database.remotekeys.RemoteKeysDao
import com.rinoindraw.storybismillah.database.story.StoryAppDatabase
import com.rinoindraw.storybismillah.database.story.StoryDao
import com.rinoindraw.storybismillah.database.story.StoryWidgetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    fun provideStoriesDao(storyDatabase: StoryAppDatabase): StoryDao = storyDatabase.getstoryDao()

    @Provides
    fun provideStoriesDaoWidget(storyDatabase: StoryAppDatabase): StoryWidgetDao = storyDatabase.getStoryDaoWidget()

    @Provides
    fun provideRemoteKeysStoryDao(storyDatabase: StoryAppDatabase): RemoteKeysDao =
        storyDatabase.remoteKeysDao()

    @Provides
    @Singleton
    fun provideStoriesDatabase(@ApplicationContext context: Context): StoryAppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StoryAppDatabase::class.java,
            "stories_database"
        ).build()
    }
}