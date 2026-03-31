# Flareon

[![License](https://img.shields.io/github/license/nathanfallet/flareon)](LICENSE)
[![Maven Central Version](https://img.shields.io/maven-central/v/me.nathanfallet.flareon/core)](https://klibs.io/project/nathanfallet/flareon)
[![Issues](https://img.shields.io/github/issues/nathanfallet/flareon)]()
[![Pull Requests](https://img.shields.io/github/issues-pr/nathanfallet/flareon)]()

A Firebase Admin SDK for Kotlin Multiplatform.

## Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    // Core module (required for credentials and authentication)
    implementation("me.nathanfallet.flareon:core:0.1.1")

    // Messaging module (for Firebase Cloud Messaging)
    implementation("me.nathanfallet.flareon:messaging:0.1.1")
}
```

### Supported Platforms

Flareon supports all Kotlin Multiplatform targets:

- **JVM** (Java 8+)
- **JavaScript** (Browser and Node.js)
- **Native**
    - iOS (arm64, x64, simulator arm64)
    - macOS (arm64, x64)
    - Linux (arm64, x64)
    - Windows (mingwX64)
    - tvOS (arm64, x64, simulator arm64)
    - watchOS (arm32, arm64, x64, device arm64, simulator arm64)

## Quick Start

### 1. Initialize Google Credentials

First, obtain a service account JSON file from your Firebase Console:

1. Go to **Project Settings** > **Service Accounts**
2. Click **Generate New Private Key**
3. Save the JSON file securely

```kotlin
import me.nathanfallet.flareon.core.credentials.GoogleCredentials

// Load from JSON string (in production, load the string from a secure location like file or environment variable)
val serviceAccountJson = """
{
  "type": "service_account",
  "project_id": "your-project-id",
  "private_key": "-----BEGIN PRIVATE KEY-----\n...",
  "client_email": "firebase-adminsdk@your-project.iam.gserviceaccount.com",
  "token_uri": "https://oauth2.googleapis.com/token"
}
""".trimIndent()

val credentials = GoogleCredentials.fromJson(serviceAccountJson)
```

### 2. Send Cloud Messages

```kotlin
import me.nathanfallet.flareon.messaging.FcmService
import me.nathanfallet.flareon.messaging.models.*

// Initialize the service
val messaging = FcmService(credentials)

// Send a simple notification
val response = messaging.sendNotification(
    token = "device-registration-token",
    title = "Hello from Flareon!",
    body = "This is a test notification"
)

println("Message sent: ${response.name}")
```

### 3. Advanced Messaging Examples

#### Send with Custom Data

```kotlin
messaging.sendNotification(
    token = "device-token",
    title = "New Message",
    body = "You have a new message",
    data = mapOf(
        "userId" to "12345",
        "messageId" to "msg_001",
        "type" to "chat"
    )
)
```

#### Send to a Topic

```kotlin
val message = FcmMessage(
    topic = "news",
    notification = FcmNotification(
        title = "Breaking News",
        body = "Check out the latest updates",
        image = "https://example.com/image.png"
    )
)

messaging.sendMessage(message)
```

#### Send with Platform-Specific Options

```kotlin
val message = FcmMessage(
    token = "device-token",
    notification = FcmNotification(
        title = "Platform-Specific Notification",
        body = "This notification has custom settings per platform"
    ),
    // Android-specific options
    android = AndroidConfig(
        priority = AndroidPriority.HIGH,
        ttl = "3600s",
        notification = AndroidNotification(
            channelId = "important_updates",
            color = "#FF5722",
            sound = "default",
            notificationPriority = AndroidNotificationPriority.HIGH,
            sticky = true
        )
    ),
    // iOS-specific options
    apns = ApnsConfig(
        payload = ApnsPayload(
            aps = Aps(
                alert = ApsAlert(
                    title = "iOS Notification",
                    body = "Custom alert for iOS",
                    sound = "default"
                ),
                badge = 1
            )
        )
    ),
    // Web Push options
    webpush = WebpushConfig(
        notification = WebpushNotification(
            title = "Web Notification",
            body = "Custom notification for web",
            icon = "https://example.com/icon.png",
            badge = "https://example.com/badge.png"
        )
    )
)

messaging.sendMessage(message)
```

#### Send to Multiple Devices

```kotlin
val tokens = listOf(
    "token1",
    "token2",
    "token3"
)

val results = messaging.sendNotificationToMultiple(
    tokens = tokens,
    title = "Bulk Notification",
    body = "Sent to multiple devices"
)

// Handle results
results.forEachIndexed { index, result ->
    result.onSuccess { response ->
        println("Message ${index + 1} sent: ${response.name}")
    }.onFailure { error ->
        println("Message ${index + 1} failed: ${error.message}")
    }
}
```

#### Conditional Targeting

```kotlin
val message = FcmMessage(
    // Target users subscribed to both 'sports' and 'cricket' topics
    condition = "'sports' in topics && 'cricket' in topics",
    notification = FcmNotification(
        title = "Cricket Match Update",
        body = "Your team is winning!"
    )
)

messaging.sendMessage(message)
```

## Modules

### Core Module (`flareon-core`)

The core module provides authentication and credential management:

- **GoogleCredentials**: Manages OAuth2 access tokens using service account credentials
- **JWT Builder**: Creates signed JWT assertions for authentication
- **HTTP Client Factory**: Provides configured Ktor clients for all platforms

Example:

```kotlin
import me.nathanfallet.flareon.core.credentials.GoogleCredentials

val credentials = GoogleCredentials.fromJson(serviceAccountJson)
val accessToken = credentials.getAccessToken() // Automatically cached and refreshed
```

### Messaging Module (`flareon-messaging`)

The messaging module provides Firebase Cloud Messaging (FCM) functionality:

- **FcmService**: Send notifications to devices, topics, or conditions
- **Platform-Specific Options**: Android, iOS (APNs), and Web Push configurations
- **Rich Notifications**: Support for images, actions, and custom data

Example:

```kotlin
import me.nathanfallet.flareon.messaging.FcmService

val messaging = FcmService(credentials)

messaging.sendNotification(
    token = "device-token",
    title = "Hello",
    body = "World"
)
```

## Advanced Configuration

### Custom HTTP Client

You can provide your own Ktor HTTP client for advanced configuration:

```kotlin
import io.ktor.client.*
import io.ktor.client.plugins.logging.*

val customClient = HttpClient {
    install(Logging) {
        level = LogLevel.ALL
    }
}

val credentials = GoogleCredentials.fromJson(
    serviceAccountJson,
    httpClient = customClient
)

val messaging = FcmService(
    credentials = credentials,
    httpClient = customClient
)
```

### Custom Cryptography Provider

For advanced use cases, you can provide a custom cryptography provider:

```kotlin
import dev.whyoleg.cryptography.CryptographyProvider

val customProvider = CryptographyProvider.Default

val credentials = GoogleCredentials.fromJson(
    serviceAccountJson,
    cryptographyProvider = customProvider
)
```
