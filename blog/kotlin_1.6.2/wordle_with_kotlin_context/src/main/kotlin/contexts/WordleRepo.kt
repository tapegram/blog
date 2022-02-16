package contexts

import arrow.core.Either
import core.Wordle
import core.WordleId

sealed class GetWordleFailure {
    data class NotFound(val id: WordleId) : GetWordleFailure()
    data class Unknown(val id: WordleId, val message: String) : GetWordleFailure()
}

sealed class SaveWordleFailure {
    data class Unknown(val id: WordleId, val message: String) : GetWordleFailure()
}

interface WordleRepo {
    fun get(id: WordleId): Either<GetWordleFailure, Wordle>
    fun save(wordle: Wordle): Either<SaveWordleFailure, Unit>
}