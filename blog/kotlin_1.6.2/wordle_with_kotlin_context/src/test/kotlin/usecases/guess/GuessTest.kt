package usecases.guess

import arrow.core.Either
import arrow.core.right
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.WordleExistsFailure
import core.Guess
import core.ValidatedChar
import core.Wordle
import core.WordleId
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import usecases.GuessContext
import usecases.Wordles
import usecases.Words
import usecases.Words.CAKES
import usecases.guess


data class DummyGuessContext(
    val wordles: MutableList<Wordle>,
    val getWordle : DummyGuessContext.(WordleId) -> Either<GetWordleFailure, Wordle?> = {
        wordles.find { curr -> curr.id == it }.right()
    },

    val saveWordle : DummyGuessContext.(Wordle) -> Either<SaveWordleFailure, Unit> = {
        wordles.removeIf { curr -> curr.id == it.id }
        wordles.add(it)
        Unit.right()
    },

    val wordleExists : DummyGuessContext.(WordleId) -> Either<WordleExistsFailure, Boolean> = {
        (wordles.find { curr -> curr.id == it } != null).right()
    },
    ) : GuessContext {

    override fun get(id: WordleId): Either<GetWordleFailure, Wordle?> = getWordle(id)

    override fun save(wordle: Wordle): Either<SaveWordleFailure, Unit> = saveWordle(wordle)

    override fun exists(id: WordleId): Either<WordleExistsFailure, Boolean> = wordleExists(id)

}

class GuessingOnFirstTry : StringSpec({
    "First try!" {
        with(DummyGuessContext(mutableListOf(Wordles.CAKES))) {
            guess(Wordles.CAKES.id, Words.CAKES) shouldBeRight Wordles.CAKES.copy(
                guesses = listOf(
                    Guess.Validated(
                        ValidatedChar.RightPlace('C'),
                        ValidatedChar.RightPlace('A'),
                        ValidatedChar.RightPlace('K'),
                        ValidatedChar.RightPlace('E'),
                        ValidatedChar.RightPlace('S'),
                    )
                )
            )
        }
    }
})