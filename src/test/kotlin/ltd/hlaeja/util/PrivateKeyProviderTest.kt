package ltd.hlaeja.util

import java.security.interfaces.RSAPrivateKey
import ltd.hlaeja.exception.KeyProviderException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PrivateKeyProviderTest {

    @Test
    fun `load private key - success`() {
        // given
        val pemFilePath = "cert/valid-private-key.pem"

        // when
        val privateKey: RSAPrivateKey = PrivateKeyProvider.load(pemFilePath)

        // then
        assertThat(privateKey).isNotNull
        assertThat(privateKey.algorithm).isEqualTo("RSA")
    }

    @Test
    fun `load private key - file does not exist`() {
        // given
        val nonExistentPemFilePath = "cert/non-existent.pem"

        // when exception
        val exception = assertThrows<KeyProviderException> {
            PrivateKeyProvider.load(nonExistentPemFilePath)
        }

        // then
        assertThat(exception.message).isEqualTo("Could not load private key")
    }

    @Test
    fun `load private key - file is invalid`() {
        // given
        val invalidPemFilePath = "cert/invalid-private-key.pem"

        // when exception
        val exception = assertThrows<IllegalArgumentException> {
            PrivateKeyProvider.load(invalidPemFilePath)
        }

        // then
        assertThat(exception.message).contains("Input byte array has wrong 4-byte ending unit")
    }
}
