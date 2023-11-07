package com.belajar.submissionintermediate.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: StoryItem)

    @Query("SELECT * FROM story_item")
    fun getAllStory(): PagingSource<Int, StoryItem>

    @Query("DELETE FROM story_item")
    fun deleteAll() : Integer
}
