package contexts

import java.util.UUID

interface Random {
    fun uuid(): UUID
}