package birthday_kata.core.domain

import arrow.core.Either

sealed class EmailSendError {
    data class ConnectionFailed(val message: String): EmailSendError()
    data class AddressDoesNotExist(val emailAddress: EmailAddress): EmailSendError()
}
interface EmailClient {
    suspend fun send(email: Message.Email): Either<EmailSendError, Unit>
}

