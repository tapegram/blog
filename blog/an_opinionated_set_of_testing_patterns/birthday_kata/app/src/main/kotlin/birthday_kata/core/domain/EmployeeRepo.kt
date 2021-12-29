package birthday_kata.core.domain

import arrow.core.Either

sealed class FindByBirthdayError {
    data class ConnectionFailed(val message: String): FindByBirthdayError()
}
sealed class SaveError {
    data class ConnectionFailed(val message: String): SaveError()
}
interface EmployeeRepo {
    suspend fun findByBirthday(dob: Birthday): Either<FindByBirthdayError, List<Employee>>
    suspend fun save(employee: Employee): Either<SaveError, Unit>
}
