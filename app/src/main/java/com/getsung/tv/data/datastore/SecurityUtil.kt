package com.getsung.tv.data.datastore

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.BLOCK_MODE_GCM
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT
import android.util.Base64
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityUtil
    @Inject
    constructor() {
        private val provider = "AndroidKeyStore"
        private val cipher by lazy {
            Cipher.getInstance("AES/GCM/NoPadding")
        }
        private val charset by lazy {
            charset("UTF-8")
        }
        private val keyStore by lazy {
            KeyStore.getInstance(provider).apply {
                load(null)
            }
        }
        private val keyGenerator by lazy {
            KeyGenerator.getInstance(KEY_ALGORITHM_AES, provider)
        }

        @Synchronized
        fun encryptData(
            keyAlias: String,
            text: String,
        ): Pair<ByteArray, ByteArray> {
            val secretKey = generateSecretKey(keyAlias)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encryptedData = cipher.doFinal(text.toByteArray(charset))
            val iv = cipher.iv
            return Pair(iv, encryptedData)
        }

        @Synchronized
        fun decryptData(
            keyAlias: String,
            iv: ByteArray,
            encryptedData: ByteArray,
        ): String {
            val secretKey = getSecretKey(keyAlias)
            val gcmParameterSpec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)
            return cipher.doFinal(encryptedData).toString(charset)
        }

        @Synchronized
        private fun generateSecretKey(keyAlias: String): SecretKey {
            val keyentry = keyStore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry
            return keyentry?.secretKey ?: run {
                keyGenerator
                    .apply {
                        init(
                            KeyGenParameterSpec
                                .Builder(keyAlias, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
                                .setBlockModes(BLOCK_MODE_GCM)
                                .setEncryptionPaddings(ENCRYPTION_PADDING_NONE)
                                .build(),
                        )
                    }.generateKey()
            }
        }

        private fun getSecretKey(keyAlias: String) = (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey

        /**
         * Encrypts [plaintext] with AES-256-GCM using an external [key] string.
         *
         * Mirrors the backend's `encryptAES` Node.js function:
         * - Key: SHA-256 hash of [key] (gives a 256-bit AES key regardless of input length)
         * - IV:  12 random bytes (GCM recommended size), generated with [SecureRandom]
         * - Tag: 128-bit GCM auth tag (appended to ciphertext by Java's Cipher, split off here)
         *
         * Returns a colon-separated Base64 string: `"IV:AuthTag:Ciphertext"`
         * — the same wire format that [decryptAES] expects.
         */
        fun encryptAES(
            plaintext: String,
            key: String,
        ): String {
            val keyBytes =
                MessageDigest
                    .getInstance("SHA-256")
                    .digest(key.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(keyBytes, "AES")

            val iv = ByteArray(12).also { SecureRandom().nextBytes(it) }
            val gcmSpec = GCMParameterSpec(128, iv)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)

            // Java appends the 16-byte auth tag at the end of doFinal output
            val ciphertextWithTag = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
            val tagOffset = ciphertextWithTag.size - 16
            val ciphertext = ciphertextWithTag.copyOfRange(0, tagOffset)
            val authTag = ciphertextWithTag.copyOfRange(tagOffset, ciphertextWithTag.size)

            val ivB64 = Base64.encodeToString(iv, Base64.NO_WRAP)
            val tagB64 = Base64.encodeToString(authTag, Base64.NO_WRAP)
            val ctB64 = Base64.encodeToString(ciphertext, Base64.NO_WRAP)

            return "$ivB64:$tagB64:$ctB64"
        }

        fun decryptAES(
            encrypted: String,
            key: String,
        ): String =
            try {
                val parts = encrypted.split(":")
                if (parts.size != 3) {
                    "****"
                } else {
                    val iv = Base64.decode(parts[0], Base64.DEFAULT)
                    val authTag = Base64.decode(parts[1], Base64.DEFAULT)
                    val ciphertext = Base64.decode(parts[2], Base64.DEFAULT)

                    val cipherInput = ciphertext + authTag

                    val keyBytes =
                        MessageDigest
                            .getInstance("SHA-256")
                            .digest(key.toByteArray(Charsets.UTF_8))
                    val keySpec = SecretKeySpec(keyBytes, "AES")
                    val gcmParameterSpec = GCMParameterSpec(128, iv)

                    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                    cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec)

                    val decryptedBytes = cipher.doFinal(cipherInput)
                    String(decryptedBytes, Charsets.UTF_8)
                }
            } catch (e: Exception) {
                "****"
            }
    }
