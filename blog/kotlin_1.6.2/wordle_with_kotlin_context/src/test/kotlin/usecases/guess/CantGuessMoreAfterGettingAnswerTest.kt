package usecases.guess

import core.Guess
import core.ValidatedChar
import core.rightPlace
import core.toWord
import core.wrong
import core.wrongPlace
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import usecases.GuessWordFailure
import usecases.Wordles
import usecases.Words
import usecases.guess


class CantGuessMoreAfterGettingAnswerTest : StringSpec({
    "Cant guess more than six times" {
        with(
            DummyGuessContext(
                mutableListOf(
                    Wordles.CAKES.copy(
                        guesses = listOf(
                            Guess.Validated(
                                'C'.rightPlace(),
                                'A'.rightPlace(),
                                'K'.rightPlace(),
                                'E'.rightPlace(),
                                'S'.rightPlace(),
                            ),
                        ),
                    ),
                )
            )
        ) {
            guess(Wordles.CAKES.id, "CRATE".toWord()) shouldBeLeft GuessWordFailure.GameIsOver(Wordles.CAKES.id)
        }
    }
})