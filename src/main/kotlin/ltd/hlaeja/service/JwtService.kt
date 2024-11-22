package ltd.hlaeja.service

import io.jsonwebtoken.Jwts
import java.security.interfaces.RSAPrivateKey
import java.util.UUID
import ltd.hlaeja.property.JwtProperty
import ltd.hlaeja.util.PrivateKeyProvider
import org.springframework.stereotype.Service

@Service
class JwtService(
    jwtProperty: JwtProperty,
) {

    private var privateKey: RSAPrivateKey = PrivateKeyProvider.load(jwtProperty.privateKey)

    suspend fun makeIdentity(device: UUID): String {
        return Jwts.builder()
            .claims()
            .add("device", device)
            .and()
            .signWith(privateKey)
            .compact()
    }
}
