package core

data class Word(
    val char1: Char,
    val char2: Char,
    val char3: Char,
    val char4: Char,
    val char5: Char,
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
        answerChar -> ValidatedChar.RightPlace(guessChar)
        else -> when (contains(guessChar)) {
            true -> ValidatedChar.WrongPlace(guessChar)
            false -> ValidatedChar.Wrong(guessChar)
        }
    }

fun String.toWord(): Word =
    // TODO: Add arrow analysis to force this to always be a 5 char string.
    Word(
        char1 = this[0],
        char2 = this[1],
        char3 = this[2],
        char4 = this[3],
        char5 = this[4],
    )