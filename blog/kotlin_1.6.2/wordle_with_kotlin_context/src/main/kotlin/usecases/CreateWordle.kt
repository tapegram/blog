import arrow.core.Either
import contexts.Dictionary
import contexts.UUIDGenerator
import contexts.SaveWordleFailure
import contexts.WordleRepo
import core.Wordle
import core.new

typealias CreateWordleFailure = SaveWordleFailure
interface CreateWordleContext: WordleRepo, UUIDGenerator, Dictionary

context(CreateWordleContext)
fun createWordle(): Either<CreateWordleFailure, Wordle> {
    val word = getWordleWord()
    val wordle = Wordle.new(
        id = uuid(),
        answer = getWordleWord(),
    )
    return save(wordle)
        .map { wordle }
}