import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import contexts.Dictionary
import contexts.UUIDGenerator
import contexts.SaveWordleFailure
import contexts.WordleExistsFailure
import contexts.WordleRepo
import core.Wordle
import core.WordleId
import core.new

sealed class CreateWordleFailure {
    data class FailedToSaveWordle(val id: WordleId, val message: String): CreateWordleFailure()
    data class FailedToLookupWordleId(val id: WordleId, val message: String): CreateWordleFailure()
    data class WordleAlreadyExists(val id: WordleId): CreateWordleFailure()
}
interface CreateWordleContext: WordleRepo, UUIDGenerator, Dictionary

context(CreateWordleContext)
suspend fun createWordle(): Either<CreateWordleFailure, Wordle> = either {
    val wordleId = uuid()

    val doesWordleExist = exists(wordleId)
        .mapLeft { it.toCreateWordleFailure()}
        .bind()

    ensure(!doesWordleExist) {
        CreateWordleFailure.WordleAlreadyExists(wordleId)
    }

    val wordle = Wordle.new(
        id = wordleId,
        answer = getWordleWord(),
    )

    save(wordle)
        .mapLeft { it.toCreateWordleFailure() }
        .bind()

    wordle
}

private fun WordleExistsFailure.toCreateWordleFailure(): CreateWordleFailure =
    when (this) {
        is WordleExistsFailure.Unknown -> CreateWordleFailure.FailedToLookupWordleId(id, message)
    }

private fun SaveWordleFailure.toCreateWordleFailure(): CreateWordleFailure =
    when (this) {
        is SaveWordleFailure.Unknown -> CreateWordleFailure.FailedToSaveWordle(id, message)
    }