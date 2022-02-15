import java.util.UUID

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
typealias WordleId = UUID

data class Wordle(
    val id: WordleId,
    val answer: Word,
    val guesses: ValidatedGuesses,
) {
    companion object {}
}

fun Wordle.guess(guess: Guess.Unvalidated): Wordle = copy(
    guesses = guesses + answer.validate(guess)
)

fun Word.contains(char: Char): Boolean =
    char in listOf(char1, char2, char3, char4, char5)

fun Word.validate(guess: Guess.Unvalidated): Guess.Validated =
    Guess.Validated(
        char1 = validateChar(guess.char1, char1),
        char2 = validateChar(guess.char2, char2),
        char3 = validateChar(guess.char3, char3),
        char4 = validateChar(guess.char4, char4),
        char5 = validateChar(guess.char5, char5),
    )

fun Word.validateChar(guessChar: Char, answerChar: Char): ValidatedChar =
    when (guessChar) {
        answerChar -> ValidatedChar.RightPlace(answerChar)
        else -> when (contains(answerChar)) {
           true -> ValidatedChar.WrongPlace(answerChar)
           false -> ValidatedChar.Wrong(answerChar)
        }
    }

fun Wordle.Companion.new(id: WordleId, answer: Word): Wordle =
    Wordle(
        id = id,
        answer = answer,
        guesses = emptyList(),
    )