package usecases.guess

import core.Wordle
import core.toWord
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import usecases.guess
import usecases.wordles.Cakes


class GuessingOnFirstTry : StringSpec({
    "First try!" {
        with(DummyGuessContext(mutableListOf(Cakes.wordle))) {
            guess(Cakes.wordle.id, "CAKES".toWord()) shouldBeRight Wordle.Complete(
                id = Cakes.wordle.id,
                guesses = listOf(
                    Cakes.Guesses.CAKES,
                ),
                answer = "CAKES".toWord(),
            )
        }
    }
})