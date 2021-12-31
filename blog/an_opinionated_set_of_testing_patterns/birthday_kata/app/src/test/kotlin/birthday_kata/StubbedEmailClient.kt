package birthday_kata

import arrow.core.Either
import arrow.core.right
import birthday_kata.core.domain.EmailClient
import birthday_kata.core.domain.Message
import birthday_kata.core.domain.EmailSendError

object StubbedEmailClient: EmailClient {
    override suspend fun send(email: Message.Email): Either<EmailSendError, Unit> =
        Unit.right()
}