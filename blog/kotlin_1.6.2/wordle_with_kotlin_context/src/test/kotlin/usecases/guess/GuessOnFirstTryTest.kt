package usecases.guess

import core.Guess
import core.ValidatedChar
import core.Wordle
import core.rightPlace
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import usecases.Cakes
import usecases.Wordles
import usecases.Words
import usecases.guess


class GuessingOnFirstTry : StringSpec({
    "First try!" {
        with(DummyGuessContext(mutableListOf(Wordles.CAKES))) {
            guess(Wordles.CAKES.id, Words.CAKES) shouldBeRight Wordle.Complete(
                id = Wordles.CAKES.id,
                guesses = listOf(
                    Cakes.Guesses.CAKES,
                ),
                answer = Words.CAKES,
            )
        }
    }
})