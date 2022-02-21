package usecases.guess

import core.toWord
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.StringSpec
import usecases.GuessWordFailure
import usecases.guess
import usecases.wordles.Cakes


class CantGuessMoreAfterGettingAnswerTest : StringSpec({
    "Cant guess more after getting the answer" {
        with(DummyGuessContext(mutableListOf(Cakes.wordle))) {
            guess(Cakes.wordle.id, "CAKES".toWord())
            guess(Cakes.wordle.id, "CRATE".toWord()) shouldBeLeft GuessWordFailure.GameIsOver(Cakes.wordle.id)
        }
    }
})