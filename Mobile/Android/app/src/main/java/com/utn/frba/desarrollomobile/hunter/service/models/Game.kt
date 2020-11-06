package com.utn.frba.desarrollomobile.hunter.service.models

import com.google.gson.annotations.SerializedName

class Game {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("creatorId")
    var creatorId: Int = 0
    @SerializedName("startDatetime")
    var startDatetime: String? = null
    @SerializedName("endDatetime")
    var endDatetime: String? = null
    @SerializedName("latitude")
    var latitude: Float = 0.toFloat()
    @SerializedName("longitude")
    var longitude: Float = 0.toFloat()
    @SerializedName("photo")
    var photo: String? = null
    @SerializedName("ended")
    var ended: Boolean? = false
    @SerializedName("winId")
    var winId: Int? = 0
    @SerializedName("winTimestamp")
    var winTimestamp: String? = null
    @SerializedName("winCode")
    var winCode: String? = null
    @SerializedName("clues")
    var clues: Array<String> = emptyArray()
    @SerializedName("userIds")
    var userIds: Array<Int> = emptyArray()
}