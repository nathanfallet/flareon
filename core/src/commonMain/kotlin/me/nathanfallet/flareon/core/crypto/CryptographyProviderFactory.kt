package me.nathanfallet.flareon.core.crypto

import dev.whyoleg.cryptography.CryptographyProvider

/**
 * Factory for obtaining the default cryptography provider.
 *
 * Uses `CryptographyProvider.Default` which automatically selects
 * the appropriate provider for each platform:
 * - JVM: JDK provider
 * - Apple (iOS/macOS/watchOS/tvOS): Apple CryptoKit
 * - Linux: OpenSSL3
 * - Windows: OpenSSL3
 * - JS: WebCrypto
 */
object CryptographyProviderFactory {
    /**
     * Gets the default cryptography provider for the current platform.
     */
    fun getDefault(): CryptographyProvider = CryptographyProvider.Default
}
