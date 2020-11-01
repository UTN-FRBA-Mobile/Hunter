package com.utn.frba.desarrollomobile.hunter.service.models

import com.google.gson.annotations.SerializedName
import java.util.*

class Game {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("creatorId")
    var creatorId: Int = 0
    @SerializedName("startDatetime")
    var startDatetime: Date? = null
    @SerializedName("endDatetime")
    var endDatetime: Date? = null
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
    var winTimestamp: Date? = null
    @SerializedName("winCode")
    var winCode: String? = null
    @SerializedName("clues")
    var clues: Array<String> = emptyArray()
    @SerializedName("userIds")
    var userIds: Array<Int> = emptyArray()
}