package usecases

import CreateWordleContext
import arrow.core.Either
import arrow.core.right
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.WordleExistsFailure
import core.Word
import core.Wordle
import core.WordleId
import java.util.UUID

data class DummyCreateWordleContext(
    val generatedUUID: UUID,
    val dictionaryWord: Word,
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

    override fun get(id: WordleId): Either<GetWordleFailure, Wordle?> =
        getWordle(id)

    override fun save(wordle: Wordle): Either<SaveWordleFailure, Unit> =
        saveWordle(wordle)

    override fun exists(id: WordleId): Either<WordleExistsFailure, Boolean> =
        wordleExists(id)

    override fun uuid(): UUID = generatedUUID

    override fun getWordleWord(): Word = dictionaryWord

}
