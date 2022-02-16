import arrow.core.Either
import arrow.core.computations.either
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

    ensure(!doesWordleExist(wordleId).bind()) {
        CreateWordleFailure.WordleAlreadyExists(wordleId)
    }

    Wordle.new(
        id = wordleId,
        answer = getWordleWord(),
    )
        .save()
        .bind()
}

context(WordleRepo)
private fun doesWordleExist(id: WordleId): Either<CreateWordleFailure, Boolean> =
    exists(id).mapLeft { it.toCreateWordleFailure()}

context(WordleRepo)
private fun Wordle.save(): Either<CreateWordleFailure, Wordle> =
    save(this)
        .mapLeft { it.toCreateWordleFailure() }
        .map { this }

private fun WordleExistsFailure.toCreateWordleFailure(): CreateWordleFailure =
    when (this) {
        is WordleExistsFailure.Unknown -> CreateWordleFailure.FailedToLookupWordleId(id, message)
    }

private fun SaveWordleFailure.toCreateWordleFailure(): CreateWordleFailure =
    when (this) {
        is SaveWordleFailure.Unknown -> CreateWordleFailure.FailedToSaveWordle(id, message)
    }