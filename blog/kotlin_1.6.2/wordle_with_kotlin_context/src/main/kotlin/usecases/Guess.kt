package usecases

import arrow.core.Either
import contexts.WordleRepo
import core.Wordle

sealed class GuessWordFailure {}

context(WordleRepo)
fun guess(word: String): Either<GuessWordFailure, Wordle> =
    TODO()

