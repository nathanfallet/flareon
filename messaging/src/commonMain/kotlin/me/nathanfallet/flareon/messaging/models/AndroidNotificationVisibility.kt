package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Android notification visibility levels.
 */
@Serializable
enum class AndroidNotificationVisibility {
    @SerialName("PRIVATE")
    PRIVATE,

    @SerialName("PUBLIC")
    PUBLIC,

    @SerialName("SECRET")
    SECRET,
}
