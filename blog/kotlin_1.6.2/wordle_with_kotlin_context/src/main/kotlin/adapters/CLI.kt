package adapters

import arrow.core.Either
import arrow.core.right
import com.github.ajalt.clikt.core.CliktCommand
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.WordleContext
import contexts.WordleExistsFailure
import core.Guess
import core.ValidatedGuesses
import core.Word
import core.Wordle
import core.WordleId
import core.toWord
import createWordle
import kotlinx.coroutines.runBlocking
import java.util.UUID

class CLI : CliktCommand() {
    override fun run() = runBlocking {
        with(wordleContext()) {
            createWordle()
                .fold(
                    { TODO() },
                    { wordle ->
                        echo(wordle.show())
                    }
                )
        }
    }
}

fun Wordle.show(): String = when (this) {
    is Wordle.InProgress -> this.guesses.show()
    is Wordle.Complete -> "Complete"
    is Wordle.Failure -> "Failure"
}

private fun ValidatedGuesses.show(): String =
    when (this.size > 1) {
        true -> this.map { it.show() }.joinToString("\n")
        false -> "No guesses"
    }

fun Guess.Validated.show(): String =
    this.toString()

fun wordleContext(): WordleContext = Context(
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