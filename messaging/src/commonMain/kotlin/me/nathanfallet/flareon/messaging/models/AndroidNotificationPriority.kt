package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Android notification priority levels.
 */
@Serializable
enum class AndroidNotificationPriority {
    @SerialName("PRIORITY_MIN")
    MIN,

    @SerialName("PRIORITY_LOW")
    LOW,

    @SerialName("PRIORITY_DEFAULT")
    DEFAULT,

    @SerialName("PRIORITY_HIGH")
    HIGH,

    @SerialName("PRIORITY_MAX")
    MAX,
}
