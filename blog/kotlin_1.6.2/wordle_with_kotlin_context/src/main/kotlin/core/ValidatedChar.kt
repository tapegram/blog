package core

sealed class ValidatedChar {
    data class RightPlace(val char: Char) : ValidatedChar()
    data class WrongPlace(val char: Char) : ValidatedChar()
    data class Wrong(val char: Char) : ValidatedChar()
}

/* Some shorthand functions to make using this slightly less verbose */
fun Char.rightPlace(): ValidatedChar = ValidatedChar.RightPlace(this)
fun Char.wrongPlace(): ValidatedChar = ValidatedChar.WrongPlace(this)
fun Char.wrong(): ValidatedChar = ValidatedChar.Wrong(this)
