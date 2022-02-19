package usecases.wordles

import core.Word
import core.Wordle

interface TestWordle {
    val wordle: Wordle.InProgress
    val dictionaryWords: List<Word>
}