package usecases.createwordle

import usecases.CreateWordleContext
import arrow.core.Either
import arrow.core.right
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.WordleExistsFailure
import core.Word
import core.Wordle
import core.WordleId
import usecases.wordles.Beans
import usecases.wordles.Cakes
import java.util.UUID

data class DummyCreateWordleContext(
    val generatedUUID: UUID,
    val dictionaryWord: Word,
    val validWords: List<Word> =  Beans.dictionaryWords + Cakes.dictionaryWords,
    val wordles: MutableList<Wordle> = mutableListOf(),

    val getWordle : DummyCreateWordleContext.(WordleId) -> Either<GetWordleFailure, Wordle?> = {
        wordles.find { curr -> curr.id == it }.right()
    },

    val saveWordle : DummyCreateWordleContext.(Wordle) -> Either<SaveWordleFailure, Unit> = {
        wordles.removeIf { curr -> curr.id == it.id }
        wordles.add(it)
        Unit.right()
    },

    val wordleExists : DummyCreateWordleContext.(WordleId) -> Either<WordleExistsFailure, Boolean> = {
        (wordles.find { curr -> curr.id == it } != null).right()
    },
): CreateWordleContext {

    /*
    WordleRepo Context
     */
    override fun get(id: WordleId): Either<GetWordleFailure, Wordle?> =
        getWordle(id)

    override fun save(wordle: Wordle): Either<SaveWordleFailure, Unit> =
        saveWordle(wordle)

    override fun exists(id: WordleId): Either<WordleExistsFailure, Boolean> =
        wordleExists(id)

    /*
    UUIDGenerator Context
     */
    override fun uuid(): UUID = generatedUUID


    /*
    Dictionary Context
     */
    override fun getWordleWord(): Word = dictionaryWord
    override fun isInDictionary(word: Word): Boolean =
        word in validWords

}
