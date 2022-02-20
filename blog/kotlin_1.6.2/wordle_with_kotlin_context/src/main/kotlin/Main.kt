import adapters.cli.CLI
import arrow.core.Either
import arrow.core.right
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.WordleContext
import contexts.WordleExistsFailure
import core.Word
import core.Wordle
import core.WordleId
import core.toWord
import java.util.UUID

fun main(args: Array<String>) = CLI(context()).main(args)

fun context(): WordleContext = Context(
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
