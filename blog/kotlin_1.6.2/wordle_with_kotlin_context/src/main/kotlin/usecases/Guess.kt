package usecases

import arrow.core.Either
import contexts.WordleRepo
import core.Word
import core.Wordle
import core.WordleId

sealed class GuessWordFailure {}

interface GuessContext : WordleRepo

context(GuessContext)
fun guess(id: WordleId, word: Word): Either<GuessWordFailure, Wordle> =
    TODO()

