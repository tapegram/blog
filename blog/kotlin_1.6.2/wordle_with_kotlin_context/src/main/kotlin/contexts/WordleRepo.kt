package contexts

import arrow.core.Either
import core.Wordle
import core.WordleId

sealed class GetWordleFailure {
    data class Unknown(val id: WordleId, val message: String) : GetWordleFailure()
}

sealed class SaveWordleFailure {
    data class Unknown(val id: WordleId, val message: String) : SaveWordleFailure()
}

sealed class WordleExistsFailure {
    data class Unknown(val id: WordleId, val message: String) : WordleExistsFailure()
}

interface WordleRepo {
    fun get(id: WordleId): Either<GetWordleFailure, Wordle?>
    fun save(wordle: Wordle): Either<SaveWordleFailure, Unit>
    fun exists(id: WordleId): Either<WordleExistsFailure, Boolean>
}