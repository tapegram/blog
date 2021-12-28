package birthday_kata

import arrow.core.Either
import arrow.core.left
import birthday_kata.core.Birthday
import birthday_kata.core.Employee
import birthday_kata.core.EmployeeRepo
import birthday_kata.core.FindByBirthdayError
import birthday_kata.core.SaveError
import java.time.LocalDate
import java.time.MonthDay

object ConnectionFailureEmployeeRepo: EmployeeRepo {
    override suspend fun findByBirthday(dob: Birthday): Either<FindByBirthdayError, List<Employee>> =
        FindByBirthdayError.ConnectionFailed(message="Oops!").left()

    override suspend fun save(employee: Employee): Either<SaveError, Unit> =
        SaveError.ConnectionFailed(message="Oops!").left()
}

private fun LocalDate.matches(monthDay: MonthDay): Boolean =
    MonthDay.from(this) == monthDay