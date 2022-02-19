package usecases.guess

import core.toWord
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.StringSpec
import usecases.Cakes
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
            guess(Wordles.CAKES.id, "CAPES".toWord())
            guess(Wordles.CAKES.id, "CAKES".toWord()) shouldBeLeft GuessWordFailure.GameIsOver(Wordles.CAKES.id)
        }
    }
})