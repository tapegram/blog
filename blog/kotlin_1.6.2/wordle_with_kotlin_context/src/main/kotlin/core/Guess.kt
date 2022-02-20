package core


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
    ): Guess() {
        override fun toString(): String =
            "$char1$char2$char3$char4$char5"

        fun toList(): List<ValidatedChar> =
            listOf(char1, char2, char3, char4, char5)
    }

}
