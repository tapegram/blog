package birthday_kata

import birthday_kata.core.BirthdayGreetingService
import birthday_kata.core.DomainName
import birthday_kata.core.EmailAddress
import birthday_kata.core.EmailResponse
import birthday_kata.core.Employee
import birthday_kata.core.Employees
import birthday_kata.core.Extension
import birthday_kata.core.Response
import io.kotest.assertions.arrow.core.shouldBeRight
import java.time.LocalDate
import java.util.UUID


private fun doug(dob: LocalDate): Employee =
    Employee(
        id = UUID.randomUUID(),
        dateOfBirth = dob,
        firstName = "Doug",
        lastName = "Dougson",
        emailAddress = EmailAddress("doug", DomainName("business", Extension.COM))
    )

private fun trixie(dob: LocalDate): Employee =
    Employee(
        id = UUID.randomUUID(),
        dateOfBirth = dob,
        firstName = "Trixie",
        lastName = "Tang",
        emailAddress = EmailAddress("trixie", DomainName("business", Extension.COM))
    )

private fun fran(dob: LocalDate): Employee =
    Employee(
        id = UUID.randomUUID(),
        dateOfBirth = dob,
        firstName = "Fran",
        lastName = "Frandottir",
        emailAddress = EmailAddress("fran", DomainName("business", Extension.COM))
    )

private fun tia(dob: LocalDate): Employee =
    Employee(
        id = UUID.randomUUID(),
        dateOfBirth = dob,
        firstName = "Tia",
        lastName = "Tiara",
        emailAddress = EmailAddress("tia", DomainName("business", Extension.COM))
    )

data class Given(
    val employees: Employees = emptyList(),
    val today: LocalDate = LocalDate.now(),
) {
    fun `No employees`() =
        this.copy(employees = emptyList())

    suspend fun `Doug, who turns 45 today`() =
        this.copy(employees = employees + doug(today.minusYears(45)))

    suspend fun `Trixie, who turns 72 today`() =
        this.copy(employees = employees + trixie(today.minusYears(72)))

    suspend fun `Fran, who turned 36 yesterday`() =
        this.copy(employees = employees + fran(today.minusDays(1).minusYears(36)))

    suspend fun `Tia, who turns 25 tomorrow`() =
        this.copy(employees = employees + tia(today.plusDays(1).minusYears(25)))

    suspend fun `when birthday emails are sent for today`(): Response = BirthdayGreetingService(
        emailClient = StubbedEmailClient,
        employeeRepo = InMemoryEmployeeRepo(employees.toMutableList())
    ).sendBirthdayEmailsForToday(today)
}

fun Response.`then Doug and Trixie should receive emails`() =
    this.also {
        it.shouldBeRight(
            listOf(
                EmailResponse(
                    subject = "Happy Birthday!",
                    to = "doug@business.com",
                    body = "Dear Doug, Happy Birthday!"
                ),
                EmailResponse(
                    subject = "Happy Birthday!",
                    to = "trixie@business.com",
                    body = "Dear Trixie, Happy Birthday!"
                )
            )
        )
    }

fun Response.`then only Doug should receive an email`() =
    this.also {
        it.shouldBeRight(
            listOf(
                EmailResponse(
                    subject = "Happy Birthday!",
                    to = "doug@business.com",
                    body = "Dear Doug, Happy Birthday!"
                ),
            )
        )
    }

fun Response.`then no one should receive an email`() =
    this.also { it.shouldBeRight(emptyList()) }