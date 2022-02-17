package usecases

import arrow.core.Either
import arrow.core.computations.either
import contexts.WordleRepo
import core.Guess
import core.Word
import core.Wordle
import core.WordleId
import core.guess

sealed class GuessWordFailure {}

interface GuessContext : WordleRepo

context(GuessContext)
suspend fun guess(
    id: WordleId,
    word: Word,
): Either<GuessWordFailure, Wordle> = either {
    getWordle(id).bind()
        .guess(word.toGuess())
        .save().bind()
}
private fun Word.toGuess(): Guess.Unvalidated =
    Guess.Unvalidated(char1, char1, char3, char4, char5)

context(WordleRepo)
private fun getWordle(id: WordleId): Either<GuessWordFailure, Wordle> =
    TODO()

context(WordleRepo)
private fun Wordle.save(): Either<GuessWordFailure, Wordle> =
    TODO()
