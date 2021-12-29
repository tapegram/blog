package birthday_kata

import arrow.core.Either
import arrow.core.right
import birthday_kata.core.domain.Email
import birthday_kata.core.domain.EmailClient
import birthday_kata.core.domain.SendError

object StubbedEmailClient: EmailClient {
    override suspend fun send(email: Email): Either<SendError, Unit> =
        Unit.right()
}