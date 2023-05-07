package com.rinoindraw.storybismillah.database.story

import com.rinoindraw.storybismillah.database.model.StoryResponse

fun storyToStoryEntity(story: StoryResponse): StoryEntity {
    return StoryEntity(
        id = story.id,
        photoUrl = story.photoUrl
    )
}