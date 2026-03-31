package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.Serializable

/**
 * APNs payload structure.
 *
 * @param aps The Apple Push Notification Service data
 */
@Serializable
data class ApnsPayload(
    val aps: Aps,
)
