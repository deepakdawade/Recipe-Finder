package com.devdd.recipe.base.firebase

/**
 * Used to subscribe users to server topics.
 */
interface TopicSubscriber {
    /**
     * Used to subscribe all users that logged in, to receive future updates.
     */
    fun subscribeToLoggedInUpdates()

    /**
     * Used to subscribe all users that logged out, to receive future updates.
     */
    fun subscribeToLoggedOutUpdates()

}