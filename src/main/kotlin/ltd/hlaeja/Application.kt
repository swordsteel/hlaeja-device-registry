package ltd.hlaeja

import ltd.hlaeja.property.JwtProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(JwtProperty::class)
@SpringBootApplication
class Application

fun main(vararg args: String) {
    runApplication<Application>(*args)
}
