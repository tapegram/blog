sealed class Message {}

sealed class ValidatedChar {
    data class RightPlace(val char: Char) : ValidatedChar()
    data class WrongPlace(val char: Char) : ValidatedChar()
    data class Wrong(val char: Char) : ValidatedChar()
}

sealed class Guess {
    data class Unvalidated(
        val char1: Char,
        val char2: Char,
        val char3: Char,
        val char4: Char,
        val char5: Char,
    ): Guess()

    data class Validated(
        val char1: ValidatedChar,
        val char2: ValidatedChar,
        val char3: ValidatedChar,
        val char4: ValidatedChar,
        val char5: ValidatedChar,
    ): Guess()
}

data class Word(
    val char1: Char,
    val char2: Char,
    val char3: Char,
    val char4: Char,
    val char5: Char,
)

typealias ValidatedGuesses = List<Guess.Validated>

data class Wordle(
    val answer: Word,
    val guesses: ValidatedGuesses,
)