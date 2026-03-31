package me.nathanfallet.flareon.core.credentials

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.RSA
import dev.whyoleg.cryptography.algorithms.SHA256
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Utility class for building and signing JWTs (JSON Web Tokens) using RS256 algorithm.
 * Used for creating assertion tokens required by Google OAuth2 service account flow.
 */
@OptIn(ExperimentalEncodingApi::class)
class JwtBuilder(
    private val cryptographyProvider: CryptographyProvider,
) {
    companion object {
        private const val GRANT_TYPE = "urn:ietf:params:oauth:grant-type:jwt-bearer"
        private const val ONE_HOUR_IN_SECONDS = 3600L
    }

    /**
     * Creates a signed JWT assertion for OAuth2 service account authentication.
     *
     * @param credentials The service account credentials containing the private key and email
     * @param scopes List of OAuth2 scopes to request
     * @return Signed JWT string
     */
    suspend fun createAssertion(
        credentials: ServiceAccountCredentials,
        scopes: List<String>,
    ): String {
        val now = Clock.System.now().epochSeconds
        val expiration = now + ONE_HOUR_IN_SECONDS

        // Build JWT header
        val header = buildJsonObject {
            put("alg", "RS256")
            put("typ", "JWT")
        }

        // Build JWT payload (claims)
        val payload = buildJsonObject {
            put("iss", credentials.clientEmail)
            put("sub", credentials.clientEmail)
            put("aud", credentials.tokenUri)
            put("scope", scopes.joinToString(" "))
            put("iat", now)
            put("exp", expiration)
        }

        // Encode header and payload
        val encodedHeader = base64UrlEncode(Json.encodeToString(header))
        val encodedPayload = base64UrlEncode(Json.encodeToString(payload))
        val signingInput = "$encodedHeader.$encodedPayload"

        // Sign the JWT
        val signature = signData(signingInput, credentials.privateKey)
        val encodedSignature = base64UrlEncode(signature)

        return "$signingInput.$encodedSignature"
    }

    /**
     * Signs data using RS256 (RSA with SHA-256).
     *
     * @param data The data to sign
     * @param privateKeyPem The private key in PEM format
     * @return The signature as a byte array
     */
    private suspend fun signData(data: String, privateKeyPem: String): ByteArray {
        // Clean the PEM format (remove headers and newlines)
        val cleanedPem = privateKeyPem
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\n", "")
            .replace("\n", "")
            .replace("\r", "")
            .trim()

        // Decode the base64 private key
        val keyBytes = Base64.decode(cleanedPem)

        // Load the private key and sign
        val rsaAlgorithm = cryptographyProvider.get(RSA.PKCS1)
        val privateKey =
            rsaAlgorithm.privateKeyDecoder(SHA256).decodeFromByteArrayBlocking(RSA.PrivateKey.Format.DER, keyBytes)

        val signatureGenerator = privateKey.signatureGenerator()
        return signatureGenerator.generateSignatureBlocking(data.encodeToByteArray())
    }

    /**
     * Encodes a string to Base64 URL-safe format (without padding).
     */
    private fun base64UrlEncode(data: String): String {
        return base64UrlEncode(data.encodeToByteArray())
    }

    /**
     * Encodes bytes to Base64 URL-safe format (without padding).
     */
    private fun base64UrlEncode(bytes: ByteArray): String {
        return Base64.UrlSafe.encode(bytes).trimEnd('=')
    }
}
