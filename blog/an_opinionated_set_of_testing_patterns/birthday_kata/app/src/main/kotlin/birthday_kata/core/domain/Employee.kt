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
    val emailAddress: EmailAddress
)

typealias FirstName = String
typealias LastName = String
typealias EmployeeId = UUID
typealias Birthday = MonthDay

fun Employee.toBirthdayEmail(): Message.Email =
    Message.Email(
        subject = "Happy Birthday!",
        to = this.emailAddress,
        body = "Dear ${this.firstName}, Happy Birthday!"
    )
