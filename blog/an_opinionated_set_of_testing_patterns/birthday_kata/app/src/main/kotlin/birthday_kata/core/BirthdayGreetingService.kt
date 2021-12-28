package birthday_kata.core

import arrow.core.Either
import arrow.core.computations.either
import java.time.Instant
import java.time.MonthDay

sealed class SendBirthdayEmailsForTodayError: Error {
    data class Unknown(val message: String): SendBirthdayEmailsForTodayError()
    object EmployeeLookupFailed: SendBirthdayEmailsForTodayError()
}

data class BirthdayGreetingService(
    val emailClient: EmailClient,
    val employeeRepo: EmployeeRepo,
) {
    suspend fun sendBirthdayEmailsForToday(
        now: Instant,
    ): Either<SendBirthdayEmailsForTodayError, Emails> = either {
        val employees =employeeRepo.findAllWithBirthdayToday(MonthDay.from(now)).bind()
        val emails = employees.toBirthdayEmails()
        emailClient.send(emails).bind()
        emails
    }
}

private suspend fun Employees.toBirthdayEmails(): Emails = TODO()
private suspend fun EmployeeRepo.findAllWithBirthdayToday(
    birthday: Birthday
): Either<SendBirthdayEmailsForTodayError, Employees> =
    this.findByBirthday(birthday)
        .mapErrors()

private fun <A> Either<FindByBirthdayError, A>.mapErrors(): Either<SendBirthdayEmailsForTodayError, A> =
    this.mapLeft {
        when (it) {
            is FindByBirthdayError.ConnectionFailed -> SendBirthdayEmailsForTodayError.EmployeeLookupFailed
        }
    }

private suspend fun EmailClient.send(emails: List<Email>): Either<SendBirthdayEmailsForTodayError, Unit> =
    TODO()