package usecases

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.WordleRepo
import core.Guess
import core.GuessFailure
import core.Word
import core.Wordle
import core.WordleId
import core.guess

sealed class GuessWordFailure {
    data class WordleNotFound(val wordleId: WordleId) : GuessWordFailure()
    data class GetWordleFailure(val wordleId: WordleId, val message: String) : GuessWordFailure()
    data class SaveWordleFailure(val wordleId: WordleId, val message: String) : GuessWordFailure()
    data class GameIsOver(val wordleId: WordleId) : GuessWordFailure()
}

interface GuessContext : WordleRepo

context(GuessContext)
suspend fun guess(
    id: WordleId,
    word: Word,
): Either<GuessWordFailure, Wordle> = either {
    getWordle(id).bind()
        .makeGuess(word.toGuess()).bind()
        .save().bind()
}

private fun Wordle.makeGuess(guess: Guess.Unvalidated): Either<GuessWordFailure, Wordle> =
    this.guess(guess)
        .mapLeft { it.toGuessWordFailure()}

private fun GuessFailure.toGuessWordFailure(): GuessWordFailure = when (this) {
    is GuessFailure.NoRemainingGuesses -> GuessWordFailure.GameIsOver(wordleId)
}

private fun Word.toGuess(): Guess.Unvalidated =
    Guess.Unvalidated(char1, char2, char3, char4, char5)

context(WordleRepo)
private fun getWordle(id: WordleId): Either<GuessWordFailure, Wordle> =
    get(id)
        .mapLeft { it.toGuessWordFailure() }
        .flatMap {
            when (it) {
                null -> GuessWordFailure.WordleNotFound(id).left()
                else -> it.right()
            }
        }

private fun GetWordleFailure.toGuessWordFailure(): GuessWordFailure =
    when (this) {
        is GetWordleFailure.Unknown -> GuessWordFailure.GetWordleFailure(id, message)
    }

context(WordleRepo)
private fun Wordle.save(): Either<GuessWordFailure, Wordle> =
    save(this)
        .mapLeft { it.toGuessWordFailure() }
        .map { this }

private fun SaveWordleFailure.toGuessWordFailure(): GuessWordFailure =
    when (this) {
        is SaveWordleFailure.Unknown -> GuessWordFailure.SaveWordleFailure(id, message)
    }
