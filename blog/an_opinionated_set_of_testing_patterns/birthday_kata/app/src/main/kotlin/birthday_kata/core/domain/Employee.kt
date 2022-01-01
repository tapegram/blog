package birthday_kata.core.domain

import java.time.LocalDate
import java.time.MonthDay
import java.util.UUID

typealias Employees = List<Employee>

data class Employee(
    val id: EmployeeId,
    val dateOfBirth: LocalDate,
    val firstName: FirstName,
    val lastName: LastName,
    val contactInfo: ContactInfo,
)

typealias FirstName = String
typealias LastName = String
typealias EmployeeId = UUID
typealias Birthday = MonthDay

data class ContactInfo(
    val phoneNumber: PhoneNumber,
    val emailAddress: EmailAddress,
    val preferredContactMethod: ContactMethod,
)

enum class ContactMethod {
    Email,
    SMS
}

fun Employee.toBirthdayMessage(): Message =
    when (this.contactInfo.preferredContactMethod) {
        ContactMethod.Email -> this.toBirthdayEmail()
        ContactMethod.SMS -> this.toBirthdaySMS()
    }

fun Employee.toBirthdaySMS(): Message.SMS =
    Message.SMS(
        number = this.contactInfo.phoneNumber,
        body = "Happy Birthday, ${this.firstName}!!"
    )

fun Employee.toBirthdayEmail(): Message.Email =
    Message.Email(
        subject = "Happy Birthday!",
        to = this.contactInfo.emailAddress,
        body = "Dear ${this.firstName}, Happy Birthday!"
    )
