package adapters

import CreateWordleFailure
import arrow.core.Either
import arrow.core.right
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.ProgramResult
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.WordleContext
import contexts.WordleExistsFailure
import core.Guess
import core.ValidatedChar
import core.ValidatedGuesses
import core.Word
import core.Wordle
import core.WordleId
import core.toWord
import createWordle
import kotlinx.coroutines.runBlocking
import usecases.GuessWordFailure
import usecases.guess
import java.util.UUID

class CLI : CliktCommand() {
    override fun run() = runBlocking {
        with(wordleContext()) {
            var wordle: Wordle = createWordle()
                .fold(
                    {
                        echo(message = it.show(), err = true)
                        throw ProgramResult(1)
                    },
                    { wordle ->
                        echo(wordle.show())
                        wordle
                    }
                )

            while (wordle is Wordle.InProgress) {
                val wordToGuess = prompt("Guess").orEmpty().uppercase()
                wordle = guess(wordle.id, wordToGuess.toWord())
                    .fold(
                        {
                            echo(message = it.show(), err = true)
                            wordle
                        },
                        {
                            echo(it.show())
                            it
                        }
                    )
            }

        }
    }
}

private fun GuessWordFailure.show(): String =
    when (this) {
        is GuessWordFailure.GameIsOver -> "Game is already over. Stop guessing."
        is GuessWordFailure.GetWordleFailure -> "Failed to fetch the wordle: $message"
        is GuessWordFailure.NotInDictionary -> "Not a recognized word"
        is GuessWordFailure.SaveWordleFailure -> "Failed to save the wordle: $message"
        is GuessWordFailure.WordleNotFound -> "Couldn't find the wordle. Are you sure it is real?"
    }

private fun CreateWordleFailure.show(): String =
    when (this) {
        is CreateWordleFailure.WordleAlreadyExists -> "Wordle already exists"

        is CreateWordleFailure.FailedToSaveWordle,
        is CreateWordleFailure.FailedToLookupWordleId -> "Technical error when connecting to DB"
    }


private fun Wordle.show(): String = when (this) {
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

private fun ValidatedGuesses.show(): String =
    when (this.size > 0) {
        true -> this.joinToString("\n") { it.show() }
        false -> "No guesses"
    }

private fun Guess.Validated.show(): String =
    this.toList().joinToString(" ") { it.show() }

private fun ValidatedChar.show(): String =
    when (this) {
        is ValidatedChar.RightPlace -> "✓$char"
        is ValidatedChar.Wrong -> "✗$char"
        is ValidatedChar.WrongPlace -> "?$char"
    }

private fun wordleContext(): WordleContext = Context(
    wordles = mutableListOf(),
)


data class Context(val wordles: MutableList<Wordle>): WordleContext {
    override fun get(id: WordleId): Either<GetWordleFailure, Wordle?> =
        wordles.find { it.id == id }.right()

    override fun save(wordle: Wordle): Either<SaveWordleFailure, Unit> {
        wordles.removeIf { curr -> curr.id == wordle.id }
        wordles.add(wordle)
        return Unit.right()
    }

    override fun exists(id: WordleId): Either<WordleExistsFailure, Boolean> =
        (wordles.find { curr -> curr.id == id } != null).right()

    override fun uuid(): UUID = UUID.randomUUID()

    override fun getWordleWord(): Word =
        // Very lazy
        "CAKES".toWord()

    override fun isInDictionary(word: Word): Boolean =
        // Even lazier.
        // If this was real we would want to do real stuff with a DB or file or anything other than this.
        true

}