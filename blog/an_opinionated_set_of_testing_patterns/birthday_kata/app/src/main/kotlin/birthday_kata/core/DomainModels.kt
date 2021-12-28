package birthday_kata.core

import arrow.core.Either
import java.time.LocalDate
import java.time.MonthDay
import java.util.UUID

interface Error

sealed class FindByBirthdayError: Error {
    data class ConnectionFailed(val message: String): FindByBirthdayError()
}
interface EmployeeRepo {
    suspend fun findByBirthday(dob: Birthday): Either<FindByBirthdayError, List<Employee>>
}

sealed class SendError: Error {
    data class ConnectionFailed(val message: String): SendError()
    data class AddressDoesNotExist(val emailAddress: EmailAddress): SendError()
}
interface EmailClient {
    suspend fun send(email: Email): Either<SendError, Unit>
}

typealias Employees = List<Employee>
typealias Emails = List<Email>

data class Employee(
    val id: EmployeeId,
    val dateOfBirth: LocalDate,
    val firstName: FirstName,
    val lastName: LastName,
    val emailAddress: EmailAddress
)

typealias FirstName = String
typealias LastName = String
typealias EmployeeId = UUID
typealias Birthday = MonthDay

fun Employee.toBirthdayEmail(): Email =
    Email(
        subject = "Happy Birthday!",
        to = this.emailAddress,
        body = "Dear ${this.firstName}, Happy Birthday!"
    )

data class EmailAddress(
    val localPart: LocalPart,
    val domainName: DomainName,
) {
    override fun toString(): String =
        "$localPart@$domainName"
}

typealias LocalPart = String

data class DomainName(
    val name: String,
    val extension: Extension
) {
    override fun toString(): String =
        "$name.$extension"
}

enum class Extension {
    COM,
    EDU,
    NET,
    ORG;

    override fun toString(): String =
        when (this) {
            COM -> "com"
            EDU -> "edu"
            NET -> "net"
            ORG -> "org"
        }
}

data class Email(
    val subject: EmailSubject,
    val to: EmailAddress,
    val body: MessageBody,
)

typealias EmailId = UUID
typealias EmailSubject = String
typealias MessageBody = String