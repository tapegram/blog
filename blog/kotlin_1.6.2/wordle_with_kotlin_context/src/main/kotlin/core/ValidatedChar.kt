package core

sealed class ValidatedChar {
    data class RightPlace(val char: Char) : ValidatedChar()
    data class WrongPlace(val char: Char) : ValidatedChar()
    data class Wrong(val char: Char) : ValidatedChar()
}
