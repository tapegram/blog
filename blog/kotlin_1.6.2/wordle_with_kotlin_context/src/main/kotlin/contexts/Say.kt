package contexts

import Message

interface Say {
    fun say(message: Message): Unit
}
