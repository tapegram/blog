package birthday_kata.core

import arrow.core.Either
import arrow.core.computations.either
import java.time.Instant
import java.time.MonthDay

sealed class SendBirthdayEmailsForTodayError: Error {
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
    val emailClient: EmailClient,
    val employeeRepo: EmployeeRepo,
) {
    suspend fun sendBirthdayEmailsForToday(
        now: Instant,
    ): Response = either {
        val employees =employeeRepo.findAllWithBirthdayToday(MonthDay.from(now)).bind()
        val emails = employees.toBirthdayEmails()
        emailClient.send(emails).bind()
        emails.toResponse()
    }
}

private fun Emails.toResponse(): EmailResponses =
    this.map { it.toResponse() }

private fun Email.toResponse(): EmailResponse =
    EmailResponse(
        subject = subject,
        to = to.toString(),
        body = body
    )

private fun Employees.toBirthdayEmails(): Emails = TODO()
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

private suspend fun EmailClient.send(emails: List<Email>): Either<SendBirthdayEmailsForTodayError, Unit> =
    TODO()