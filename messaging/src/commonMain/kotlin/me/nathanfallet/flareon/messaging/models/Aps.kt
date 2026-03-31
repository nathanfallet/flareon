package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Apple Push Notification Service data.
 *
 * @param alert Alert payload (can be a string or ApsAlert object)
 * @param badge Badge number to display on the app icon
 * @param sound Sound to play (can be a string or CriticalSound object)
 * @param contentAvailable Whether to wake the app in background
 * @param mutableContent Whether the notification is mutable
 * @param category Notification category identifier
 * @param threadId Thread identifier for grouping notifications
 */
@Serializable
data class Aps(
    val alert: ApsAlert? = null,
    val badge: Int? = null,
    val sound: String? = null,
    @SerialName("content-available")
    val contentAvailable: Int? = null,
    @SerialName("mutable-content")
    val mutableContent: Int? = null,
    val category: String? = null,
    @SerialName("thread-id")
    val threadId: String? = null,
)
