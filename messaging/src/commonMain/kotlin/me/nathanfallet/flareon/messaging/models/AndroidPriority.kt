package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Android message priority levels.
 */
@Serializable
enum class AndroidPriority {
    @SerialName("high")
    HIGH,

    @SerialName("normal")
    NORMAL,
}
