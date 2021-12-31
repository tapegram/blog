package birthday_kata

import arrow.core.Either
import arrow.core.right
import birthday_kata.core.domain.Message
import birthday_kata.core.domain.SMSClient
import birthday_kata.core.domain.SMSSendError

object StubbedSMSClient: SMSClient {
    override suspend fun send(email: Message.SMS): Either<SMSSendError, Unit> =
        Unit.right()
}