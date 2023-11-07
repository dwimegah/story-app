package com.belajar.submissionintermediate

import com.belajar.submissionintermediate.data.database.StoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryItem> {
        val items: MutableList<StoryItem> = mutableListOf()
        for(i in 0..100){
            val story = StoryItem(
                id = i.toString(),
                "photoUrl + $i",
                "createdAt + $i",
                "name + $i",
                "description + $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}