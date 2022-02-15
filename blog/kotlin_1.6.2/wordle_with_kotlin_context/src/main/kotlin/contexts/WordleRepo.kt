package contexts

import Wordle
import WordleId
import arrow.core.Either

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