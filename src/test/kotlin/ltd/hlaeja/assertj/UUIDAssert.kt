package ltd.hlaeja.assertj

import java.util.UUID
import org.assertj.core.api.AbstractAssert

class UUIDAssert(actual: UUID) : AbstractAssert<UUIDAssert, UUID>(actual, UUIDAssert::class.java) {
    fun isUUID(expected: String): UUIDAssert {
        objects.assertEqual(this.info, this.actual, UUID.fromString(expected))
        return this.myself
    }
}

fun assertThat(actual: UUID): UUIDAssert {
    return UUIDAssert(actual)
}
