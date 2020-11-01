package com.utn.frba.desarrollomobile.hunter.service.models

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("sub")
    var sub: String? = null
    @SerializedName("alias")
    var alias: String? = null
    @SerializedName("mail")
    var mail: String? = null
    @SerializedName("firstName")
    var firstName: String? = null
    @SerializedName("lastName")
    var lastName: String? = null
}