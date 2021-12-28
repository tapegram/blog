package birthday_kata

import birthday_kata.core.BirthdayGreetingService
import birthday_kata.core.DomainName
import birthday_kata.core.EmailAddress
import birthday_kata.core.Employee
import birthday_kata.core.Employees
import birthday_kata.core.Extension
import birthday_kata.core.Response
import java.time.Instant
import java.time.Period
import java.util.UUID


private fun doug(dob: Instant): Employee =
    Employee(
        id = UUID.randomUUID(),
        dateOfBirth = dob,
        firstName = "Doug",
        lastName = "Dougson",
        emailAddress = EmailAddress("doug", DomainName("business", Extension.COM))
    )

private fun trixie(dob: Instant): Employee =
    Employee(
        id = UUID.randomUUID(),
        dateOfBirth = dob,
        firstName = "Trixie",
        lastName = "Tang",
        emailAddress = EmailAddress("trixie", DomainName("business", Extension.COM))
    )

private fun fran(dob: Instant): Employee =
    Employee(
        id = UUID.randomUUID(),
        dateOfBirth = dob,
        firstName = "Fran",
        lastName = "Frandottir",
        emailAddress = EmailAddress("fran", DomainName("business", Extension.COM))
    )

private fun tia(dob: Instant): Employee =
    Employee(
        id = UUID.randomUUID(),
        dateOfBirth = dob,
        firstName = "Tia",
        lastName = "Tiara",
        emailAddress = EmailAddress("tia", DomainName("business", Extension.COM))
    )

data class Given(
    val employees: Employees = emptyList(),
    val today: Instant = Instant.now(),
) {
    fun `No employees`() = this

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

fun Instant.minusDays(days: Int): Instant =
    this.minus(Period.ofDays(days))

fun Instant.plusDays(days: Int): Instant =
    this.plus(Period.ofDays(days))

fun Instant.minusYears(years: Int): Instant =
    this.minus(Period.ofYears(years))