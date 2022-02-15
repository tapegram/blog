import arrow.core.Either
import contexts.Dictionary
import contexts.Random
import contexts.SaveWordleFailure
import contexts.WordleRepo

object Service {
    context(Dictionary, WordleRepo, Random)
    fun createWordle(): Either<CreateWordleFailure, Wordle> {
        val word = getWordleWord()
        val wordle = Wordle.new(
            id = uuid(),
            answer = getWordleWord(),
        )
        return save(wordle)
            .map { wordle }
    }


    context(WordleRepo)
    fun guess(word: String): Either<GuessWordFailure, Wordle> =
        TODO()
}

typealias CreateWordleFailure = SaveWordleFailure
sealed class GuessWordFailure {}