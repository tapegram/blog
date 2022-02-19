package usecases

import core.Guess
import core.Wordle
import core.rightPlace
import core.wrong
import core.wrongPlace

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

object Cakes {
    object Guesses {
        val CRANE = Guess.Validated(
            'C'.rightPlace(),
            'R'.wrong(),
            'A'.wrongPlace(),
            'N'.wrong(),
            'E'.wrongPlace(),
        )
        val CRABS = Guess.Validated(
            'C'.rightPlace(),
            'R'.wrong(),
            'A'.wrongPlace(),
            'B'.wrong(),
            'S'.rightPlace(),
        )
        val CANTS = Guess.Validated(
            'C'.rightPlace(),
            'A'.rightPlace(),
            'N'.wrong(),
            'T'.wrong(),
            'S'.rightPlace(),
        )
        val CASES = Guess.Validated(
            'C'.rightPlace(),
            'A'.rightPlace(),
            'S'.wrongPlace(),
            'E'.rightPlace(),
            'S'.rightPlace(),
        )
        val CAWES = Guess.Validated(
            'C'.rightPlace(),
            'A'.rightPlace(),
            'W'.wrong(),
            'E'.rightPlace(),
            'S'.rightPlace(),
        )
        val CAKED = Guess.Validated(
            'C'.rightPlace(),
            'A'.rightPlace(),
            'K'.rightPlace(),
            'E'.rightPlace(),
            'D'.wrong(),
        )
        val CAKES = Guess.Validated(
            'C'.rightPlace(),
            'A'.rightPlace(),
            'K'.rightPlace(),
            'E'.rightPlace(),
            'S'.rightPlace(),
        )
    }
}