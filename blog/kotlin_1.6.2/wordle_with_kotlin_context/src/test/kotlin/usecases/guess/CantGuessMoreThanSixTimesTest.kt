package usecases.guess

import core.toWord
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.StringSpec
import usecases.GuessWordFailure
import usecases.guess
import usecases.wordles.Cakes


class CantGuessMoreThanSixTimesTest : StringSpec({
    "Cant guess more than six times" {
        with(
            DummyGuessContext(
                mutableListOf(
                    Cakes.wordle.copy(
                        guesses = listOf(
                            Cakes.Guesses.CRANE,
                            Cakes.Guesses.CRABS,
                            Cakes.Guesses.CANTS,
                            Cakes.Guesses.CASES,
                            Cakes.Guesses.CAWES,
                        ),
                    ),
                )
            )
        ) {
            guess(Cakes.wordle.id, "CAPES".toWord())
            guess(Cakes.wordle.id, "CAKES".toWord()) shouldBeLeft GuessWordFailure.GameIsOver(Cakes.wordle.id)
        }
    }
})