package com.devdd.recipe.firebase

import com.devdd.recipe.base.firebase.TopicSubscriber
import com.devdd.recipe.base.utils.extensions.debugElseRelease
import com.devdd.recipe.base_android.utils.extensions.buildType
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

class FcmTopicSubscriber(
    private val firebaseMessaging: FirebaseMessaging,
) : TopicSubscriber {

    override fun subscribeToLoggedInUpdates() {
        try {
            firebaseMessaging.subscribeToTopic(prefixForTopic(TOPIC_LOGGED_IN))

            // Unsubscribe logged out updates
            firebaseMessaging.unsubscribeFromTopic(prefixForTopic(TOPIC_LOGGED_OUT))
        } catch (e: Exception) {
            Timber.e(e, "Error subscribing to logged in topics")
        }
    }

    override fun subscribeToLoggedOutUpdates() {
        try {
            firebaseMessaging.subscribeToTopic(prefixForTopic(TOPIC_LOGGED_OUT))

            // Unsubscribe logged in updates
            firebaseMessaging.unsubscribeFromTopic(prefixForTopic(TOPIC_LOGGED_IN))
        } catch (e: Exception) {
            Timber.e(e, "Error subscribing to logged out topics")
        }
    }

    private fun prefixForTopic(topic: String) =
        buildType.debugElseRelease({ "rf_dev_$topic" }, { topic })

    companion object {
        private const val TOPIC_LOGGED_IN = "topic_logged_in"
        private const val TOPIC_LOGGED_OUT = "topic_not_logged_in"
    }
}