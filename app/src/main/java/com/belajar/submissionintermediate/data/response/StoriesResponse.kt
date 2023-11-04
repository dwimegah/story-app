package com.belajar.submissionintermediate.data.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoriesResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<Story>,
    @SerializedName("message")
    val message: String
)
@Parcelize
data class Story(
    @field:SerializedName("createdAt")
    val createdAt: String,
    @field:SerializedName("description")
    val description: String,
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("photoUrl")
    val photoUrl: String
): Parcelable
