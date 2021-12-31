package birthday_kata.core.domain

import arrow.core.Either

sealed class MessageSendError {
    data class ConnectionFailed(val message: String): MessageSendError()
    data class TargetDoesNotExist(val message: String): MessageSendError()
}

data class MessageClient (
    val emailClient: EmailClient,
    val smsClient: SMSClient,
){
    suspend fun send(message: Message): Either<MessageSendError, Unit> =
        when (message) {
            is Message.Email -> emailClient.send(message).mapEmailErrors()
            is Message.SMS -> smsClient.send(message).mapSMSErrors()
        }
}

private fun <A> Either<EmailSendError, A>.mapEmailErrors(): Either<MessageSendError, A> = this.mapLeft {
    when (it) {
        is EmailSendError.AddressDoesNotExist -> MessageSendError.TargetDoesNotExist(it.emailAddress.toString())
        is EmailSendError.ConnectionFailed -> MessageSendError.ConnectionFailed(it.message)
    }
}

private fun <A> Either<SMSSendError, A>.mapSMSErrors(): Either<MessageSendError, A> = this.mapLeft {
    when (it) {
        is SMSSendError.NumberDoesNotExist -> MessageSendError.TargetDoesNotExist(it.phoneNumber)
        is SMSSendError.ConnectionFailed -> MessageSendError.ConnectionFailed(it.message)
    }
}


