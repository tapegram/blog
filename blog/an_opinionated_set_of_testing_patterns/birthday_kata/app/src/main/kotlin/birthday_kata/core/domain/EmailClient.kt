package birthday_kata.core.domain

import arrow.core.Either

sealed class SendError {
    data class ConnectionFailed(val message: String): SendError()
    data class AddressDoesNotExist(val emailAddress: EmailAddress): SendError()
}
interface EmailClient {
    suspend fun send(email: Email): Either<SendError, Unit>
}

