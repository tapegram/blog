package usecases

import CreateWordleContext
import CreateWordleFailure
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import contexts.Dictionary
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.UUIDGenerator
import contexts.WordleExistsFailure
import core.Word
import core.Wordle
import core.WordleId
import createWordle
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import java.util.UUID

val CAKES = Word('C', 'A', 'K', 'E', 'S')
val BEANS = Word('B', 'E', 'A', 'N', 'S')
val aUUID = UUID.randomUUID()

data class DummyContext(
    val generatedUUID: UUID,
    val dictionaryWord:  Word,
    val wordles: MutableList<Wordle> = mutableListOf(),
    val getWordle : DummyContext.(WordleId) -> Either<GetWordleFailure, Wordle?> = {
        wordles.find { curr -> curr.id == it }.right()
    },
    val saveWordle : DummyContext.(Wordle) -> Either<SaveWordleFailure, Unit> = {
        wordles.removeIf { curr -> curr.id == it.id }
        wordles.add(it)
        Unit.right()
    },
    val wordleExists : DummyContext.(WordleId) -> Either<WordleExistsFailure, Boolean> = {
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


class CreateWordleTest : StringSpec({
    "Create a new wordle game" {
        with(DummyContext(aUUID, CAKES, mutableListOf())) {
            createWordle() shouldBeRight Wordle(
                id = aUUID,
                answer = CAKES,
            )
        }
    }

    "Can't create a new wordle game for an existing wordle ID" {
        // In retrospect, this is only needed if we think there will be duplicate UUIDs, which should basically never have in real life.
        // But whatever.
        with(
            DummyContext(
                generatedUUID = aUUID,
                dictionaryWord = CAKES,
                wordles = mutableListOf(
                    Wordle(
                        id = aUUID,
                        answer = BEANS,
                    )
                ),
            )
        ) {
            createWordle() shouldBeLeft CreateWordleFailure.WordleAlreadyExists(
                id = aUUID,
            )
        }
    }

    "Unknown failure when saving wordle" {
        with(
            DummyContext(
                generatedUUID = aUUID,
                dictionaryWord = CAKES,
                wordles = mutableListOf(),
                saveWordle = {
                    SaveWordleFailure.Unknown(
                        id = aUUID,
                        message = "Oops"
                    ).left()
                }
            )
        ) {
            createWordle() shouldBeLeft CreateWordleFailure.FailedToSaveWordle(
                id = aUUID,
                message = "Oops"
            )
        }
    }
})