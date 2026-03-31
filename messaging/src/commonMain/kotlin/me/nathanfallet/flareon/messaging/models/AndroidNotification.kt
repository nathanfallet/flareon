package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Android-specific notification options.
 *
 * @param title Android notification title (overrides Notification.title)
 * @param body Android notification body (overrides Notification.body)
 * @param icon Icon resource name
 * @param color Icon color in #RRGGBB format
 * @param sound Sound file name or "default"
 * @param tag Notification tag for replacement
 * @param clickAction Action to trigger on click
 * @param bodyLocKey Body localization key
 * @param bodyLocArgs Body localization arguments
 * @param titleLocKey Title localization key
 * @param titleLocArgs Title localization arguments
 * @param channelId Android notification channel ID (required for Android O+)
 * @param image Image URL (overrides Notification.image)
 * @param ticker Ticker text for accessibility
 * @param sticky Whether notification persists on click
 * @param eventTime Event time in RFC3339 UTC format
 * @param localOnly Whether notification is local-only
 * @param notificationPriority Notification priority level
 * @param vibrateTimings Vibration pattern as duration strings
 * @param defaultVibrateTimings Use default vibration pattern
 * @param defaultSound Use default sound
 * @param lightSettings LED light settings
 * @param defaultLightSettings Use default light settings
 * @param visibility Visibility level
 * @param notificationCount Badge count
 */
@Serializable
data class AndroidNotification(
    val title: String? = null,
    val body: String? = null,
    val icon: String? = null,
    val color: String? = null,
    val sound: String? = null,
    val tag: String? = null,
    @SerialName("click_action")
    val clickAction: String? = null,
    @SerialName("body_loc_key")
    val bodyLocKey: String? = null,
    @SerialName("body_loc_args")
    val bodyLocArgs: List<String>? = null,
    @SerialName("title_loc_key")
    val titleLocKey: String? = null,
    @SerialName("title_loc_args")
    val titleLocArgs: List<String>? = null,
    @SerialName("channel_id")
    val channelId: String? = null,
    val image: String? = null,
    val ticker: String? = null,
    val sticky: Boolean? = null,
    @SerialName("event_time")
    val eventTime: String? = null,
    @SerialName("local_only")
    val localOnly: Boolean? = null,
    @SerialName("notification_priority")
    val notificationPriority: AndroidNotificationPriority? = null,
    @SerialName("vibrate_timings")
    val vibrateTimings: List<String>? = null,
    @SerialName("default_vibrate_timings")
    val defaultVibrateTimings: Boolean? = null,
    @SerialName("default_sound")
    val defaultSound: Boolean? = null,
    @SerialName("light_settings")
    val lightSettings: LightSettings? = null,
    @SerialName("default_light_settings")
    val defaultLightSettings: Boolean? = null,
    val visibility: AndroidNotificationVisibility? = null,
    @SerialName("notification_count")
    val notificationCount: Int? = null,
)
