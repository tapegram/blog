package core

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.util.UUID

typealias ValidatedGuesses = List<Guess.Validated>
typealias WordleId = UUID

data class Wordle(
    val id: WordleId,
    val answer: Word,
    val guesses: ValidatedGuesses = emptyList(),
) {
    companion object {}
}

sealed class GuessFailure {
    data class NoRemainingGuesses(val wordleId: WordleId) : GuessFailure()
}

fun Wordle.guess(guess: Guess.Unvalidated): Either<GuessFailure, Wordle> =
    // TODO: Instead of checking the len of guesses, can we make Wordle into a state machine?
    when (!this.isGameOver()) {
        true -> copy(guesses = guesses + answer.validate(guess)).right()
        false -> GuessFailure.NoRemainingGuesses(id).left()
    }

fun Wordle.isGameOver(): Boolean =
    guesses.size >= 6 || guesses.any { it.isCorrect() }

fun Guess.Validated.isCorrect(): Boolean =
    this.char1 is ValidatedChar.RightPlace &&
    this.char2 is ValidatedChar.RightPlace &&
    this.char3 is ValidatedChar.RightPlace &&
    this.char4 is ValidatedChar.RightPlace &&
    this.char5 is ValidatedChar.RightPlace

fun Wordle.Companion.new(id: WordleId, answer: Word): Wordle =
    Wordle(
        id = id,
        answer = answer,
        guesses = emptyList(),
    )
