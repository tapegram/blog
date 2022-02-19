package usecases.guess

import arrow.core.Either
import arrow.core.right
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.WordleExistsFailure
import core.Word
import core.Wordle
import core.WordleId
import usecases.GuessContext
import usecases.wordles.Beans
import usecases.wordles.Cakes

data class DummyGuessContext(
    val wordles: MutableList<Wordle>,
    val validWords: List<Word> =  Beans.dictionaryWords + Cakes.dictionaryWords,
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

    override fun getWordleWord(): Word = TODO("Not used in our tests. Maybe this means we should split the context up?")

    override fun isInDictionary(word: Word): Boolean =
        word in validWords
}

