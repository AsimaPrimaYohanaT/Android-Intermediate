package com.example.mystoryapp.response

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ListStoryResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem> ,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
@Entity(tableName = "story")
data class ListStoryItem(

	@field:SerializedName("photoUrl")
	var photoUrl: String? = null,

	@field:SerializedName("createdAt")
	var createdAt: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("description")
	var description: String? = null,

	@field:SerializedName("lon")
	var lon: Double ,

	@PrimaryKey
	@NonNull
	@field:SerializedName("id")
	var id: String,

	@field:SerializedName("lat")
	var lat: Double
):Parcelable
