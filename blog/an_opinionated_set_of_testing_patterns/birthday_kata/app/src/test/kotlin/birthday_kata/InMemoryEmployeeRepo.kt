package birthday_kata

import arrow.core.Either
import arrow.core.right
import birthday_kata.core.domain.Birthday
import birthday_kata.core.domain.Employee
import birthday_kata.core.domain.EmployeeRepo
import birthday_kata.core.domain.FindByBirthdayError
import birthday_kata.core.domain.SaveError
import java.time.LocalDate
import java.time.MonthDay

data class InMemoryEmployeeRepo(val employees: MutableList<Employee>): EmployeeRepo {
    override suspend fun findByBirthday(dob: Birthday): Either<FindByBirthdayError, List<Employee>> =
        employees.filter { it.dateOfBirth.matches(dob) }.right()

    override suspend fun save(employee: Employee): Either<SaveError, Unit> {
        employees.add(employee)
        return Unit.right()
    }
}

private fun LocalDate.matches(monthDay: MonthDay): Boolean =
    MonthDay.from(this) == monthDay