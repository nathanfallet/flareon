package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * LED light settings for Android notifications.
 *
 * @param color LED color in #RRGGBB format
 * @param lightOnDuration Light on duration (e.g., "3.5s")
 * @param lightOffDuration Light off duration (e.g., "3.5s")
 */
@Serializable
data class LightSettings(
    val color: String,
    @SerialName("light_on_duration")
    val lightOnDuration: String,
    @SerialName("light_off_duration")
    val lightOffDuration: String,
)
