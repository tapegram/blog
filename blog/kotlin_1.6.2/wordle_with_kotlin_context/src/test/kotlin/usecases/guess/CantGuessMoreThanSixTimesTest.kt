package usecases.guess

import core.Guess
import core.rightPlace
import core.toWord
import core.wrong
import core.wrongPlace
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.StringSpec
import usecases.GuessWordFailure
import usecases.Wordles
import usecases.guess


class CantGuessMoreThanSixTimesTest : StringSpec({
    "Cant guess more than six times" {
        with(
            DummyGuessContext(
                mutableListOf(
                    Wordles.CAKES.copy(
                        guesses = listOf(
                            Guess.Validated(
                                'C'.rightPlace(),
                                'R'.wrong(),
                                'A'.wrongPlace(),
                                'N'.wrong(),
                                'E'.wrongPlace(),
                            ),
                            Guess.Validated(
                                'C'.rightPlace(),
                                'R'.wrong(),
                                'A'.wrongPlace(),
                                'B'.wrong(),
                                'S'.rightPlace(),
                            ),
                            Guess.Validated(
                                'C'.rightPlace(),
                                'A'.rightPlace(),
                                'N'.wrong(),
                                'T'.wrong(),
                                'S'.rightPlace(),
                            ),
                            Guess.Validated(
                                'C'.rightPlace(),
                                'A'.rightPlace(),
                                'S'.wrongPlace(),
                                'E'.rightPlace(),
                                'S'.rightPlace(),
                            ),
                            Guess.Validated(
                                'C'.rightPlace(),
                                'A'.rightPlace(),
                                'W'.wrong(),
                                'E'.rightPlace(),
                                'S'.rightPlace(),
                            ),
                            Guess.Validated(
                                'C'.rightPlace(),
                                'A'.rightPlace(),
                                'P'.wrong(),
                                'E'.rightPlace(),
                                'S'.rightPlace(),
                            ),
                        ),
                    ),
                )
            )
        ) {
            guess(Wordles.CAKES.id, "CAKES".toWord()) shouldBeLeft GuessWordFailure.GameIsOver(Wordles.CAKES.id)
        }
    }
})