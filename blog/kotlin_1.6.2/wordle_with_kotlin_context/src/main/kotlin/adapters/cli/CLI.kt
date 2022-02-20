package adapters.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.output.TermUi.echo
import contexts.WordleContext
import core.Wordle
import core.toWord
import usecases.createWordle
import kotlinx.coroutines.runBlocking
import usecases.CreateWordleFailure
import usecases.GuessWordFailure
import usecases.guess

data class CLI(val context: WordleContext) : CliktCommand() {
    override fun run() = runBlocking {
        with(context) {
            var wordle: Wordle = createWordle()
                .fold(
                    ::handleCreateWordleFailure,
                    ::handleCreateWordleSuccess,
                )

            while (wordle is Wordle.InProgress) {
                val wordToGuess = prompt("Guess").orEmpty().uppercase()
                wordle = guess(wordle.id, wordToGuess.toWord())
                    .fold(
                        {
                            handleGuessFailure(it)
                            wordle
                        },
                        ::handleGuessSuccess,
                    )
            }
        }
    }
}

/**
 * We can choose to be wayyyy more rigourous in our failure handling here.
 * For instance, we may decide that some failure for guesses (like repo failures) should
 * terminate the game, or we could retry up to X number of times before giving up.
 *
 * Similarly, we could make creating the wordle try a few more times for possibly transient "DB"
 * errors before exiting.
 */

fun handleCreateWordleFailure(failure: CreateWordleFailure): Nothing {
    echo(message = failure.show(), err = true)
    throw ProgramResult(1)
}

fun handleCreateWordleSuccess(wordle: Wordle): Wordle =
    wordle.also { echo(it.show()) }

fun handleGuessFailure(failure: GuessWordFailure): Unit =
    echo(message = failure.show(), err = true)

fun handleGuessSuccess(wordle: Wordle): Wordle =
    wordle.also { echo(it.show()) }