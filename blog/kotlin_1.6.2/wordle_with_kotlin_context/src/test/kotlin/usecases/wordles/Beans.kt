package usecases.wordles

import core.Wordle
import core.toWord
import java.util.UUID

object Beans {
    val wordle = Wordle.InProgress(
        id = UUID.randomUUID(),
        answer = "BEANS".toWord(),
    )
}

