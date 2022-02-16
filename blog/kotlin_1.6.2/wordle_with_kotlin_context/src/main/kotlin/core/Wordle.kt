package core

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

fun Wordle.guess(guess: Guess.Unvalidated): Wordle = copy(
    guesses = guesses + answer.validate(guess)
)

fun Wordle.Companion.new(id: WordleId, answer: Word): Wordle =
    Wordle(
        id = id,
        answer = answer,
        guesses = emptyList(),
    )
