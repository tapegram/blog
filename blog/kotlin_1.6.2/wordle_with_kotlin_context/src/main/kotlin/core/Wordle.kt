package core

import arrow.core.Either
import arrow.core.right
import java.util.UUID

typealias ValidatedGuesses = List<Guess.Validated>
typealias WordleId = UUID

sealed class Wordle {
    companion object {}

    data class InProgress(
        override val id: WordleId,
        override val guesses: ValidatedGuesses = emptyList(),
        override val answer: Word
    ) : Wordle()

    data class Failure(
        override val id: WordleId,
        override val guesses: ValidatedGuesses,
        override val answer: Word
    ) : Wordle()

    data class Complete(
        override val id: WordleId,
        override val guesses: ValidatedGuesses,
        override val answer: Word
    ) : Wordle()

    abstract val id: WordleId
    abstract val guesses: ValidatedGuesses
    abstract val answer: Word
}

sealed class GuessFailure {
    data class NoRemainingGuesses(val wordleId: WordleId) : GuessFailure()
}

fun Wordle.InProgress.guess(guess: Guess.Unvalidated): Either<GuessFailure, Wordle> =
    copy(guesses = guesses + answer.validate(guess))
        .handleSuccess()
        .handleFailure()
        .right()

fun Wordle.Companion.new(id: WordleId, answer: Word): Wordle.InProgress =
    Wordle.InProgress(
        id = id,
        answer = answer,
        guesses = emptyList(),
    )

private fun Wordle.handleSuccess(): Wordle =
    when (this) {
        is Wordle.InProgress -> when (this.hasCorrectGuess()) {
            true -> Wordle.Complete(id, guesses, answer)
            false -> this
        }

        is Wordle.Complete,
        is Wordle.Failure -> this
    }

private fun Wordle.handleFailure(): Wordle =
    when (this) {
        is Wordle.InProgress -> when (this.hasRemainingGuesses()) {
            true -> this
            false -> Wordle.Failure(id, guesses, answer)
        }

        is Wordle.Complete,
        is Wordle.Failure -> this
    }

private fun Wordle.InProgress.hasRemainingGuesses(): Boolean =
    guesses.size < 6

private fun Wordle.InProgress.hasCorrectGuess(): Boolean =
    guesses.any { it.isCorrect() }

private fun Guess.Validated.isCorrect(): Boolean =
    this.char1 is ValidatedChar.RightPlace &&
    this.char2 is ValidatedChar.RightPlace &&
    this.char3 is ValidatedChar.RightPlace &&
    this.char4 is ValidatedChar.RightPlace &&
    this.char5 is ValidatedChar.RightPlace
