package ltd.hlaeja.util

import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64.getDecoder
import ltd.hlaeja.exception.KeyProviderException

object PrivateKeyProvider {

    fun load(
        pemFile: String,
    ): RSAPrivateKey = readPrivatePemFile(pemFile)
        .let(::makePrivateKey)

    private fun makePrivateKey(
        privateKeyBytes: ByteArray,
    ): RSAPrivateKey = KeyFactory.getInstance("RSA")
        .generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateKey

    private fun readPrivatePemFile(
        privateKey: String,
    ): ByteArray = javaClass.classLoader
        .getResource(privateKey)
        ?.readText()
        ?.let(::getPrivateKeyByteArray)
        ?: throw KeyProviderException("Could not load private key")

    private fun getPrivateKeyByteArray(
        keyText: String,
    ): ByteArray = keyText.replace(Regex("[\r\n]+"), "")
        .removePrefix("-----BEGIN PRIVATE KEY-----")
        .removeSuffix("-----END PRIVATE KEY-----")
        .let { getDecoder().decode(it) }
}
