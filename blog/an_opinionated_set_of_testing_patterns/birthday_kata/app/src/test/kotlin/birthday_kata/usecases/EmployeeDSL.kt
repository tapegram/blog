package birthday_kata.usecases

import birthday_kata.core.domain.DomainName
import birthday_kata.core.domain.EmailAddress
import birthday_kata.core.domain.Employee
import birthday_kata.core.domain.Extension
import birthday_kata.core.domain.FirstName
import birthday_kata.core.domain.LastName
import birthday_kata.core.domain.LocalPart
import java.time.LocalDate
import java.util.UUID

/*
Based on blog post
https://www.raywenderlich.com/2780058-domain-specific-languages-in-kotlin-getting-started
 */
data class EmployeeBuilder(
    var id: UUID = UUID.randomUUID(),
    var dateOfBirth: LocalDate = LocalDate.now().minusYears(30),
    var firstName: FirstName = "Doug",
    var lastName: LastName = "Dougson",
    var emailAddress: EmailAddress = EmailAddress("doug", DomainName("business", Extension.COM))
) {
    inline fun id(id: EmployeeBuilder.() -> UUID) {
       this.id = id()
    }

    inline fun dateOfBirth(dateOfBirth: EmployeeBuilder.() -> LocalDate) {
        this.dateOfBirth = dateOfBirth()
    }

    inline fun firstName(firstName: EmployeeBuilder.() -> FirstName) {
        this.firstName = firstName()
    }

    inline fun lastName(lastName: EmployeeBuilder.() -> LastName) {
        this.lastName = lastName()
    }

    inline fun emailAddress(lambda: EmailAddressBuilder.() -> Unit) {
        this.emailAddress= EmailAddressBuilder().apply(lambda).build()
    }

    fun build() = Employee(
        id = id,
        dateOfBirth = dateOfBirth,
        firstName = firstName,
        lastName = lastName,
        emailAddress = emailAddress,
    )
}

data class EmailAddressBuilder(
    var localPart: LocalPart = "doug",
    var domain: String = "business",
    var extension: Extension = Extension.COM,
) {
    inline fun localPart(localPart: EmailAddressBuilder.() -> LocalPart) {
        this.localPart = localPart()
    }

    inline fun domain(domain: EmailAddressBuilder.() -> String) {
        this.domain = domain()
    }

    inline fun extension(extension: EmailAddressBuilder.() -> Extension) {
        this.extension = extension()
    }

    fun build() = EmailAddress(
        localPart = localPart,
        domainName = DomainName(
            name = domain,
            extension = extension,
        )
    )
}

fun employee(lambda: EmployeeBuilder.() -> Unit): Employee =
    EmployeeBuilder().apply(lambda).build()
