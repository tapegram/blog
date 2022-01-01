package birthday_kata

import birthday_kata.core.BirthdayGreetingService
import birthday_kata.core.MessageResponse
import birthday_kata.core.Response
import birthday_kata.core.SendBirthdayEmailsForTodayError
import birthday_kata.core.domain.EmailClient
import birthday_kata.core.domain.Employee
import birthday_kata.core.domain.EmployeeRepo
import birthday_kata.core.domain.MessageClient
import birthday_kata.core.domain.SMSClient
import birthday_kata.usecases.employee
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldBeIn
import java.time.LocalDate


private fun doug(dob: LocalDate): Employee =
    employee {
        dateOfBirth { dob }
        firstName { "Doug" }
        lastName { "Dougson" }
        contactInfo {
            emailAddress {
                localPart { "doug" }
            }
        }
    }

private fun charles(dob: LocalDate): Employee =
    employee {
        dateOfBirth { dob }
        firstName { "Charles" }
        lastName { "TheThird" }
        contactInfo {
            phoneNumber { "555-111-1111" }
            emailAddress {
                localPart { "charles" }
            }
        }
    }

private fun louise(dob: LocalDate): Employee =
    employee {
        dateOfBirth { dob }
        firstName { "Louise" }
        lastName { "Jeeze" }
        contactInfo {
            phoneNumber { "555-222-2222" }
            emailAddress {
                localPart { "louise" }
            }
        }
    }

private fun trixie(dob: LocalDate): Employee =
    employee {
        dateOfBirth { dob }
        firstName { "Trixie" }
        lastName { "Tang" }
        contactInfo {
            emailAddress {
                localPart { "trixie" }
            }
        }
    }

private fun fran(dob: LocalDate): Employee =
    employee {
        dateOfBirth { dob }
        firstName { "Fran" }
        lastName { "Frandottir" }
        contactInfo {
            emailAddress {
                localPart { "fran" }
            }
        }
    }

private fun tia(dob: LocalDate): Employee =
    employee {
        dateOfBirth { dob }
        firstName { "Tia" }
        lastName { "Tiara" }
        contactInfo {
            emailAddress {
                localPart { "tia" }
            }
        }
    }

data class Given(
    val employeeRepo: EmployeeRepo = InMemoryEmployeeRepo(mutableListOf()),
    val emailClient: EmailClient = StubbedEmailClient,
    val smsClient: SMSClient = StubbedSMSClient,
    val today: LocalDate = LocalDate.now(),
) {
    fun `No employees`() =
        this.copy(employeeRepo = InMemoryEmployeeRepo(mutableListOf()))

    suspend fun `Doug, who turns 45 today`(): Given {
        employeeRepo.save(doug(today.minusYears(45)))
        return this
    }

    suspend fun `Trixie, who turns 72 today`(): Given {
        employeeRepo.save(trixie(today.minusYears(72)))
        return this
    }

    suspend fun `Fran, who turned 36 yesterday`(): Given {
        employeeRepo.save(fran(today.minusDays(1).minusYears(36)))
        return this
    }

    suspend fun `Tia, who turns 25 tomorrow`(): Given {
        employeeRepo.save(tia(today.plusDays(1).minusYears(25)))
        return this
    }

    suspend fun `Charles, who turns 15 today and preferres SMS`(): Given {
        employeeRepo.save(charles(today.minusYears(15)))
        return this
    }

    suspend fun `Louise, who turns 71 today and preferres SMS`(): Given {
        employeeRepo.save(louise(today.minusYears(71)))
        return this
    }

    fun `a persistent connection failure when connecting to the Employee database`(): Given =
        this.copy(employeeRepo = ConnectionFailureEmployeeRepo)

    suspend fun `when birthday emails are sent for today`(): Response = BirthdayGreetingService(
        messageClient = MessageClient(
            emailClient = emailClient,
            smsClient = smsClient,
        ),
        employeeRepo = employeeRepo,
    ).sendBirthdayEmailsForToday(today)
}

fun Response.`then Charles and Louise should receive SMSs`() =
    this.also {
        it.shouldBeRight(
            listOf(
                MessageResponse.SMSResponse(
                    number = "555-111-1111",
                    body = "Happy Birthday, Charles!!",
                ),
                MessageResponse.SMSResponse(
                    number = "555-222-2222",
                    body = "Happy Birthday, Louise!!",
                ),
            )
        )
    }

fun Response.`then only Charles should receive an SMS`() =
    this.also {
        it.shouldBeRight(
            listOf(
                MessageResponse.SMSResponse(
                    number = "555-111-1111",
                    body = "Happy Birthday, Charles!!",
                )
            )
        )
    }

fun Response.`then Charles should receive an SMS`() =
    this.also {
        it.shouldBeRight()
        it.tap { messages ->
            MessageResponse.SMSResponse(
                number = "555-111-1111",
                body = "Happy Birthday, Charles!!",
            ).shouldBeIn(messages)
        }
    }

fun Response.`then Doug and Trixie should receive emails`() =
    this.also {
        it.shouldBeRight(
            listOf(
                MessageResponse.EmailResponse(
                    subject = "Happy Birthday!",
                    to = "doug@business.com",
                    body = "Dear Doug, Happy Birthday!"
                ),
                MessageResponse.EmailResponse(
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
                MessageResponse.EmailResponse(
                    subject = "Happy Birthday!",
                    to = "doug@business.com",
                    body = "Dear Doug, Happy Birthday!"
                ),
            )
        )
    }

fun Response.`then Doug should receive an email`() =
    this.also {
        it.shouldBeRight()
        it.tap { messages ->
            MessageResponse.EmailResponse(
                subject = "Happy Birthday!",
                to = "doug@business.com",
                body = "Dear Doug, Happy Birthday!"
            ).shouldBeIn(messages)
        }
    }

fun Response.`then no one should receive an email`() =
    this.also { it.shouldBeRight(emptyList()) }

fun Response.`then an employee lookup failed error should be returned`() =
    this.also { it.shouldBeLeft(SendBirthdayEmailsForTodayError.EmployeeLookupFailed) }