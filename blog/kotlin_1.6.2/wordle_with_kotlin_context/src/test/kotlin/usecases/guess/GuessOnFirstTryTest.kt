package usecases.guess

import core.Guess
import core.ValidatedChar
import core.rightPlace
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import usecases.Wordles
import usecases.Words
import usecases.guess


class GuessingOnFirstTry : StringSpec({
    "First try!" {
        with(DummyGuessContext(mutableListOf(Wordles.CAKES))) {
            guess(Wordles.CAKES.id, Words.CAKES) shouldBeRight Wordles.CAKES.copy(
                guesses = listOf(
                    Guess.Validated(
                        'C'.rightPlace(),
                        'A'.rightPlace(),
                        'K'.rightPlace(),
                        'E'.rightPlace(),
                        'S'.rightPlace(),
                    )
                )
            )
        }
    }
})