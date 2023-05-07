package com.rinoindraw.storybismillah.utils

import com.rinoindraw.storybismillah.database.model.Story
import com.rinoindraw.storybismillah.database.model.StoryResponse

object DataDummy {
    fun generateDummyListStoriesResponse() : List<Story> {
        val listStories = ArrayList<Story>()

        for (i in 0..10) {
            val stories = Story(
                id = "story-I6srsq7uD6v-6syX",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1682866401036_8X_5KAaA.jpg",
                createdAt = "023-04-30T14:53:21.037Z",
                name = "Rino",
                description = "hola",
                lon = -106.80391,
                lat = -6.2246685
            )
            listStories.add(stories)
        }
        return listStories
    }
}