package usecases.guess

import core.toWord
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.StringSpec
import usecases.GuessWordFailure
import usecases.guess
import usecases.wordles.Cakes

class MustGuessRealWordsTest : StringSpec({
    "Guesses must be real words in the dictionary" {
        with(DummyGuessContext(mutableListOf(Cakes.wordle))) {
            guess(Cakes.wordle.id, "EMBGN".toWord()) shouldBeLeft GuessWordFailure.NotInDictionary(Cakes.wordle.id, "EMBGN")
        }
    }
})
