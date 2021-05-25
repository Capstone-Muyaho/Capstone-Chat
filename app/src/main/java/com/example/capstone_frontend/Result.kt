package com.example.capstone_frontend

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Result {
    @SerializedName("result")
    @Expose
    var result: Int? = null

    @SerializedName("imageUri")
    @Expose
    var imageUri: String? = null

}