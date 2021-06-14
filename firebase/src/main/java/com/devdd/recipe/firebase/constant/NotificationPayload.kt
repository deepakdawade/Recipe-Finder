package com.devdd.recipe.firebase.constant

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationPayload(

    @Expose
    @SerializedName("body")
    val body: String?,

    @Expose
    @SerializedName("en")
    val en: String?,

    @Expose
    @SerializedName("nid")
    val nid: Long,

    @Expose
    @SerializedName("hi")
    val hi: String?,

    @Expose
    @SerializedName("sound")
    val sound: Boolean?,

    @Expose
    @SerializedName("additionalData")
    val additionalData: String?,

    @Expose
    @SerializedName("msgData")
    val msgData: String?,

    @Expose
    @SerializedName("silent")
    val silent: Boolean?,

    @Expose
    @SerializedName("title")
    val title: String?,

    @Expose
    @SerializedName("message")
    val message: String?,

    @Expose
    @SerializedName("imageURL")
    val imageURL: String?,

    @Expose
    @SerializedName("externalLink")
    val externalLink: String?,

    @Expose
    @SerializedName("action")
    val action: String,

    @Expose
    @SerializedName("channelId")
    val channelId: String?,

    @Expose
    @SerializedName("otp")
    val otp: String?,

    @Expose
    @SerializedName("opsnid")
    val opsnid: Long?,
)