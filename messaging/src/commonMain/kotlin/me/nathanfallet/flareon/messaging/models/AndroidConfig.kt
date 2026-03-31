package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Android-specific notification configuration.
 *
 * @param collapseKey Collapse key for message grouping
 * @param priority The message priority (HIGH or NORMAL)
 * @param ttl Time-to-live as duration string (e.g., "3600s")
 * @param restrictedPackageName Package name restriction
 * @param data Android-specific data fields (overrides Message.data)
 * @param notification Android-specific notification options
 * @param directBootOk Whether to deliver in direct boot mode
 */
@Serializable
data class AndroidConfig(
    @SerialName("collapse_key")
    val collapseKey: String? = null,
    val priority: AndroidPriority? = null,
    val ttl: String? = null,
    @SerialName("restricted_package_name")
    val restrictedPackageName: String? = null,
    val data: Map<String, String>? = null,
    val notification: AndroidNotification? = null,
    @SerialName("direct_boot_ok")
    val directBootOk: Boolean? = null,
)
