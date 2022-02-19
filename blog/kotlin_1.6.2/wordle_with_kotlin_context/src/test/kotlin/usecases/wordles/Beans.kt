package usecases.wordles

import core.Wordle
import core.toWord
import java.util.UUID

object Beans: TestWordle {
    override val wordle = Wordle.InProgress(
        id = UUID.randomUUID(),
        answer = "BEANS".toWord(),
    )

    override val dictionaryWords = listOf(
        "BEANS",
    ).map { it.toWord() }
}

