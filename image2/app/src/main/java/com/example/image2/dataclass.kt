package com.example.image2

import com.google.gson.annotations.SerializedName


data class PreprocessedImageResponse(
    @SerializedName("processedImageURL")
    val processedImageURL: String // Change this to match the actual field name in your JSON response
)
