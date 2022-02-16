package usecases

import CreateWordleContext
import arrow.core.Either
import arrow.core.right
import contexts.Dictionary
import contexts.GetWordleFailure
import contexts.SaveWordleFailure
import contexts.UUIDGenerator
import core.Word
import core.Wordle
import core.WordleId
import createWordle
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import java.util.UUID

val CAKES = Word('C', 'A', 'K', 'E', 'S')
val aUUID = UUID.randomUUID()

data class DummyContext(
    val generatedUUID: UUID,
    val dictionaryWord:  Word,
    val wordles: MutableList<Wordle>
): CreateWordleContext {
    override fun get(id: WordleId): Either<GetWordleFailure, Wordle> =
        Either.fromNullable(
            wordles.find { it.id == id }
        ).mapLeft { GetWordleFailure.NotFound(id) }

    override fun save(wordle: Wordle): Either<SaveWordleFailure, Unit> {
        wordles.removeIf { it.id == wordle.id }
        wordles.add(wordle)
        return Unit.right()
    }

    override fun uuid(): UUID = generatedUUID

    override fun getWordleWord(): Word = dictionaryWord
}


class CreateWordleTest : StringSpec({
    "Create a new wordle game" {
        with(DummyContext(aUUID, CAKES, mutableListOf())) {
            createWordle() shouldBeRight Wordle(
                id = aUUID,
                answer = CAKES,
            )
        }
    }
})