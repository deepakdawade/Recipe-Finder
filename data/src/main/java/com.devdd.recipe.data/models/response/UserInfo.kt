package com.devdd.recipe.data.models.response

import com.google.firebase.auth.FirebaseUser

data class UserInfo(

    val id: String?,

    val name: String?,

    val email: String?,

    val phonenumber: String?
)

fun FirebaseUser.toUserInfo(): UserInfo {
    return UserInfo(
        email = this.email,
        name = this.displayName,
        phonenumber = this.phoneNumber,
        id = this.uid,
    )
}