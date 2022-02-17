package usecases.guess

import arrow.core.Either
import arrow.core.right
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.WordleExistsFailure
import core.Wordle
import core.WordleId
import usecases.GuessContext

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

    override fun get(id: WordleId): Either<GetWordleFailure, Wordle?> =
        getWordle(id)

    override fun save(wordle: Wordle): Either<SaveWordleFailure, Unit> =
        saveWordle(wordle)

    override fun exists(id: WordleId): Either<WordleExistsFailure, Boolean> =
        wordleExists(id)
}

