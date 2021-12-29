package birthday_kata.specs

import birthday_kata.Given
import birthday_kata.`then Doug and Trixie should receive emails`
import birthday_kata.`then no one should receive an email`
import birthday_kata.`then only Doug should receive an email`
import io.kotest.core.spec.style.StringSpec

class EmailTest : StringSpec({
    "Should generate no emails if there are no employees" {
        Given()
            .`No employees`()
            .`when birthday emails are sent for today`()
            .`then no one should receive an email`()
    }

    "Should generate no emails if it is no one's birthday" {
        Given()
            .`Fran, who turned 36 yesterday`()
            .`Tia, who turns 25 tomorrow`()
            .`when birthday emails are sent for today`()
            .`then no one should receive an email`()
    }

    "Should generate an email for Doug on his birthday" {
        Given()
            .`Doug, who turns 45 today`()
            .`Fran, who turned 36 yesterday`()
            .`Tia, who turns 25 tomorrow`()
            .`when birthday emails are sent for today`()
            .`then only Doug should receive an email`()
    }

    "Should generate emails for Doug and Trixie on their shared birthday" {
        Given()
            .`Doug, who turns 45 today`()
            .`Trixie, who turns 72 today`()
            .`Fran, who turned 36 yesterday`()
            .`Tia, who turns 25 tomorrow`()
            .`when birthday emails are sent for today`()
            .`then Doug and Trixie should receive emails`()
    }
})
