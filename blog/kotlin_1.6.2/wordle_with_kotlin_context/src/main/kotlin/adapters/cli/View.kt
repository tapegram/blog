package adapters.cli

import core.Guess
import core.ValidatedChar
import core.ValidatedGuesses
import core.Wordle
import usecases.CreateWordleFailure
import usecases.GuessWordFailure

 fun GuessWordFailure.show(): String =
    when (this) {
        is GuessWordFailure.GameIsOver -> "Game is already over. Stop guessing."
        is GuessWordFailure.GetWordleFailure -> "Failed to fetch the wordle: $message"
        is GuessWordFailure.NotInDictionary -> "Not a recognized word"
        is GuessWordFailure.SaveWordleFailure -> "Failed to save the wordle: $message"
        is GuessWordFailure.WordleNotFound -> "Couldn't find the wordle. Are you sure it is real?"
    }

 fun CreateWordleFailure.show(): String =
    when (this) {
        is CreateWordleFailure.WordleAlreadyExists -> "Wordle already exists"

        is CreateWordleFailure.FailedToSaveWordle,
        is CreateWordleFailure.FailedToLookupWordleId -> "Technical error when connecting to DB"
    }


fun Wordle.show(): String = when (this) {
    is Wordle.InProgress -> this.guesses.show()

    is Wordle.Complete -> """
You got it! ${this.guesses.size}/6
${this.guesses.show()}
    """.trimIndent()

    is Wordle.Failure -> """
You blew it! X/6
The answer was ${this.answer.toString()}
${this.guesses.show()}
    """.trimIndent()
}

fun ValidatedGuesses.show(): String =
    when (this.size > 0) {
        true -> this.joinToString("\n") { it.show() }
        false -> "No guesses"
    }

fun Guess.Validated.show(): String =
    this.toList().joinToString(" ") { it.show() }

fun ValidatedChar.show(): String =
    when (this) {
        is ValidatedChar.RightPlace -> "✓$char"
        is ValidatedChar.Wrong -> "✗$char"
        is ValidatedChar.WrongPlace -> "?$char"
    }
