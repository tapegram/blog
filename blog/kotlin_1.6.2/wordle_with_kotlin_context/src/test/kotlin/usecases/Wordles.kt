package usecases

import core.Wordle

object Wordles {
    val CAKES = Wordle.InProgress(
        id = WordleIds.id1,
        answer = Words.CAKES
    )
    val BEANS = Wordle.InProgress(
        id = WordleIds.id2,
        answer = Words.BEANS
    )
}