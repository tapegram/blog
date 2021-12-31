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
import birthday_kata.core.domain.toBirthdayEmail
import java.time.LocalDate
import java.time.MonthDay

sealed class SendBirthdayEmailsForTodayError {
    data class Unknown(val message: String): SendBirthdayEmailsForTodayError()
    object EmployeeLookupFailed: SendBirthdayEmailsForTodayError()
}

typealias Response = Either<SendBirthdayEmailsForTodayError, EmailResponses>

data class EmailResponse(
    val subject: String,
    val to: String,
    val body: String,
)

typealias EmailResponses = List<EmailResponse>

data class BirthdayGreetingService(
    val messageClient: MessageClient,
    val employeeRepo: EmployeeRepo,
) {
    suspend fun sendBirthdayEmailsForToday(
        now: LocalDate,
    ): Response = either {
        val employees =employeeRepo.findAllWithBirthdayToday(MonthDay.from(now)).bind()
        val messages = employees.toBirthdayMessages()
        messageClient.send(messages).bind()
        messages.toResponse()
    }
}

private fun Messages.toResponse(): EmailResponses =
    this.map { it.toResponse() }

private fun Message.toResponse(): EmailResponse =
    when (this) {
        is Message.Email -> this.toResponse()
        is Message.SMS -> TODO("Fill this in when in the next PR")
    }

private fun Message.Email.toResponse(): EmailResponse =
    EmailResponse(
        subject = subject,
        to = to.toString(),
        body = body
    )

private fun Employees.toBirthdayMessages(): Messages =
    this.map { it.toBirthdayEmail() }

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