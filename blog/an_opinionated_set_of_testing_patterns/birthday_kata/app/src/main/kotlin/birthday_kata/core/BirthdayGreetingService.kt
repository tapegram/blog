package birthday_kata.core

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.right
import birthday_kata.core.domain.Birthday
import birthday_kata.core.domain.EmployeeRepo
import birthday_kata.core.domain.Employees
import birthday_kata.core.domain.FindByBirthdayError
import birthday_kata.core.domain.Message
import birthday_kata.core.domain.MessageClient
import birthday_kata.core.domain.Messages
import birthday_kata.core.domain.toBirthdayMessage
import java.time.LocalDate
import java.time.MonthDay

sealed class SendBirthdayEmailsForTodayError {
    data class Unknown(val message: String): SendBirthdayEmailsForTodayError()
    object EmployeeLookupFailed: SendBirthdayEmailsForTodayError()
}

typealias Response = Either<SendBirthdayEmailsForTodayError, MessageResponses>

sealed class MessageResponse {
    data class EmailResponse(
        val subject: String,
        val to: String,
        val body: String,
    ): MessageResponse()

    data class SMSResponse(
        val number: String,
        val body: String,
    ): MessageResponse()
}

typealias MessageResponses = List<MessageResponse>

data class BirthdayGreetingService(
    val messageClient: MessageClient,
    val employeeRepo: EmployeeRepo,
) {
    suspend fun sendBirthdayEmailsForToday(
        now: LocalDate,
    ): Response = either {
        val employees = employeeRepo
            .findAllWithBirthdayToday(MonthDay.from(now))
            .bind()
        val messages = employees.toBirthdayMessages()
        messageClient.send(messages).bind()
        messages.toResponse()
    }
}

private fun Messages.toResponse(): MessageResponses =
    this.map { it.toResponse() }

private fun Message.toResponse(): MessageResponse =
    when (this) {
        is Message.Email -> this.toEmailResponse()
        is Message.SMS -> this.toSMSResponse()
    }

private fun Message.Email.toEmailResponse(): MessageResponse.EmailResponse =
    MessageResponse.EmailResponse(
        subject = subject,
        to = to.toString(),
        body = body
    )

private fun Message.SMS.toSMSResponse(): MessageResponse.SMSResponse =
    MessageResponse.SMSResponse(
        number = number,
        body = body,
    )

private fun Employees.toBirthdayMessages(): Messages =
    this.map { it.toBirthdayMessage() }

private suspend fun EmployeeRepo.findAllWithBirthdayToday(
    birthday: Birthday
): Either<SendBirthdayEmailsForTodayError, Employees> =
    this.findByBirthday(birthday)
        .mapErrors()

private fun <A> Either<FindByBirthdayError, A>.mapErrors(): Either<SendBirthdayEmailsForTodayError, A> =
    this.mapLeft {
        when (it) {
            is FindByBirthdayError.ConnectionFailed ->
                SendBirthdayEmailsForTodayError.EmployeeLookupFailed
        }
    }

private suspend fun MessageClient.send(emails: Messages): Either<SendBirthdayEmailsForTodayError, Unit> =
    // Coding to happy path for now, until we add more tests
    emails.forEach { this.send(it) }.right()