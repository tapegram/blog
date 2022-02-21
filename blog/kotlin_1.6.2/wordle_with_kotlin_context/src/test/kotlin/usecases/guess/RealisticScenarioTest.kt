package usecases.guess

import core.Wordle
import core.toWord
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import usecases.guess
import usecases.wordles.Cakes


class RealisticScenarioTest : StringSpec({
    /*
    Answer:  CAKES
    Guess:   CRANE
    Guess:   CAKED
    Guess:   CAKES
    */
    "'CAKES' 1/6 - CRANE" {
        with(DummyGuessContext(mutableListOf(Cakes.wordle))) {
            guess(Cakes.wordle.id, "CRANE".toWord()) shouldBeRight Cakes.wordle.copy(
                guesses = listOf(
                    Cakes.Guesses.CRANE,
                )
            )
        }
    }

    "'CAKES' 2/6 - CAKED" {
        with(
            DummyGuessContext(
                mutableListOf(
                    Cakes.wordle.copy(
                        guesses = listOf(
                            Cakes.Guesses.CRANE,
                        ),
                    ),
                )
            )
        ) {
            guess(Cakes.wordle.id, "CAKED".toWord()) shouldBeRight Cakes.wordle.copy(
                guesses = listOf(
                    Cakes.Guesses.CRANE,
                    Cakes.Guesses.CAKED,
                )
            )
        }
    }

    "'CAKES' 3/6 - CAKES" {
        with(
            DummyGuessContext(
                mutableListOf(
                    Cakes.wordle.copy(
                        guesses = listOf(
                            Cakes.Guesses.CRANE,
                            Cakes.Guesses.CAKED,
                        ),
                    ),
                )
            )
        ) {
            guess(Cakes.wordle.id, "CAKES".toWord()) shouldBeRight Wordle.Complete(
                id = Cakes.wordle.id,
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