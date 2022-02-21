package contexts

import core.Word

interface Dictionary {
    fun getWordleWord(): Word
    fun isInDictionary(word: Word): Boolean
}