package birthday_kata

import arrow.core.Either
import arrow.core.left
import birthday_kata.core.domain.Birthday
import birthday_kata.core.domain.Employee
import birthday_kata.core.domain.EmployeeRepo
import birthday_kata.core.domain.FindByBirthdayError
import birthday_kata.core.domain.SaveError

object ConnectionFailureEmployeeRepo: EmployeeRepo {
    override suspend fun findByBirthday(dob: Birthday): Either<FindByBirthdayError, List<Employee>> =
        FindByBirthdayError.ConnectionFailed(message="Oops!").left()

    override suspend fun save(employee: Employee): Either<SaveError, Unit> =
        SaveError.ConnectionFailed(message="Oops!").left()
}