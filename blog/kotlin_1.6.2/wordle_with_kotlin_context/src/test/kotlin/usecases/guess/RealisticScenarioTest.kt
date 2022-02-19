package usecases.guess

import core.Guess
import core.ValidatedChar
import core.Wordle
import core.rightPlace
import core.toWord
import core.wrong
import core.wrongPlace
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import usecases.Cakes
import usecases.Wordles
import usecases.guess


class RealisticScenarioTest : StringSpec({
    /*
    Answer:  CAKES
    Guess: CRANE
    Guess: CAKED
    Guess: CAKES
    */
    "'CAKES' 1/6 - CRANE" {
        with(DummyGuessContext(mutableListOf(Wordles.CAKES))) {
            guess(Wordles.CAKES.id, "CRANE".toWord()) shouldBeRight Wordles.CAKES.copy(
                guesses = listOf(
                    Guess.Validated(
                        'C'.rightPlace(),
                        'R'.wrong(),
                        'A'.wrongPlace(),
                        'N'.wrong(),
                        'E'.wrongPlace(),
                    ),
                )
            )
        }
    }

    "'CAKES' 2/6 - CAKED" {
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
                        ),
                    ),
                )
            )
        ) {
            guess(Wordles.CAKES.id, "CAKED".toWord()) shouldBeRight Wordles.CAKES.copy(
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
                        'A'.rightPlace(),
                        'K'.rightPlace(),
                        'E'.rightPlace(),
                        'D'.wrong(),
                    ),
                )
            )
        }
    }

    "'CAKES' 3/6 - CAKES" {
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
                                'A'.rightPlace(),
                                'K'.rightPlace(),
                                'E'.rightPlace(),
                                'D'.wrong(),
                            ),
                        ),
                    ),
                )
            )
        ) {
            guess(Wordles.CAKES.id, "CAKES".toWord()) shouldBeRight Wordle.Complete(
                id = Wordles.CAKES.id,
                answer = "CAKES".toWord(),
                guesses = listOf(
                    Cakes.Guesses.CRANE,
                    Cakes.Guesses.CAKED,
                    Cakes.Guesses.CAKES,
                )
            )
        }
    }
})