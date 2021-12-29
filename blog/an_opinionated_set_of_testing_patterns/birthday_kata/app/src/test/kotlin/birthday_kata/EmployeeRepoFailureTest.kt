package birthday_kata

import io.kotest.core.spec.style.StringSpec

class EmployeeRepoFailureTest : StringSpec({
    "Should generate no emails if there are no employees" {
        Given()
            .`a persistent connection failure when connecting to the Employee database`()
            .`when birthday emails are sent for today`()
            .`then an employee lookup failed error should be returned`()
    }
})
