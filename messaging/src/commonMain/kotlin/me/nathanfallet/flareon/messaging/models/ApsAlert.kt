package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * APNs alert payload with localization support.
 *
 * @param title Alert title
 * @param subtitle Alert subtitle
 * @param body Alert body text
 * @param titleLocKey Title localization key
 * @param titleLocArgs Title localization arguments
 * @param subtitleLocKey Subtitle localization key
 * @param subtitleLocArgs Subtitle localization arguments
 * @param bodyLocKey Body localization key
 * @param bodyLocArgs Body localization arguments
 * @param actionLocKey Action button localization key
 * @param launchImage Launch image filename
 */
@Serializable
data class ApsAlert(
    val title: String? = null,
    val subtitle: String? = null,
    val body: String? = null,
    @SerialName("title-loc-key")
    val titleLocKey: String? = null,
    @SerialName("title-loc-args")
    val titleLocArgs: List<String>? = null,
    @SerialName("subtitle-loc-key")
    val subtitleLocKey: String? = null,
    @SerialName("subtitle-loc-args")
    val subtitleLocArgs: List<String>? = null,
    @SerialName("loc-key")
    val bodyLocKey: String? = null,
    @SerialName("loc-args")
    val bodyLocArgs: List<String>? = null,
    @SerialName("action-loc-key")
    val actionLocKey: String? = null,
    @SerialName("launch-image")
    val launchImage: String? = null,
)
