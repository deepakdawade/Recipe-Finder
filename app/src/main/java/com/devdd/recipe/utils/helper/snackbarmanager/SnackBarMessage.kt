package com.devdd.exampleapp.commonui.utils.snackbarmanager

data class SnackBarMessage(
    /** Server error code */
    val errorCode: Int? = null,

    /** Resource string ID of the message to show */
    val msgResId: Int,

    val localizedMessage: String? = null,

    /** Optional resource string ID for the action (example: "Got it!") */
    val actionId: Int? = null,

    /** Set to true for a Snackbar with long duration  */
    val longDuration: Boolean = false,

    /** Optional change ID to avoid repetition of messages */
    val requestChangeId: String? = null
)
