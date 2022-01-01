package birthday_kata.core.domain

import arrow.core.Either

sealed class SMSSendError {
    data class ConnectionFailed(val message: String): SMSSendError()
    data class NumberDoesNotExist(val phoneNumber: PhoneNumber): SMSSendError()
}

interface SMSClient {
    suspend fun send(email: Message.SMS): Either<SMSSendError, Unit>
}

