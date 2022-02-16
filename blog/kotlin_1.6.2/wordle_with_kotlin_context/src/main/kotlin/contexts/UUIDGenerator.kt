package contexts

import java.util.UUID

fun interface UUIDGenerator {
    fun uuid(): UUID
}