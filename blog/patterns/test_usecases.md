# TEST USECASES

## Problem Context
If you are [HIDING THE DOMAIN](hide_the_domain.md) or just generally trying to maximize refactorability when applying [TEST DRIVEN DEVELOPMENT](test_driven_development.md), there is naturally a big question that arises: What do I actually test?

Testing the domain models directly would be everyone's first impulse and it makes sense! They are the actual business logic so it's critical they be correct. And if you are applying [PORTS AND ADAPTERS](ports_and_adapters.md) or are [KEEPING THE DOMAIN PURE](keep_the_domain_pure.md) then those objects are trivial to test exhaustively.

However, for the same reason we HIDE THE DOMAIN, we don't want to test them directly. That would lead to coupling from our dozens or hundreds of exhaustive unittests towards the domain, which would make refactoring a pain. Every change would need to update every test and after a bit of that, you look to avoid refactoring.

But they still need to be tested, so what do you do?

## Application
The domain model needs to be able to change constantly, but the business ["use cases"](https://en.wikipedia.org/wiki/Use_case) are fairly stable. In other words, the "domain logic" needs to be refactored constantly, but the "application logic" needs to stay the same. So we test at the "application" level. With PORTS AND ADAPTERS, that would be the service level.

So write all of your tests at the service level and in the language of the business use cases. I try to make sure all my stories are written with acceptance criteria in Given/When/Then format so they can be directly translated in the tests.

## Resulting Context
As a result of writing your tests like this you will

1) Make the "definition of done" simpler for your work. Implement the AC as Usecase tests and when they pass, you are probably done.
2) Build up test cases that do not need to be updated when the domain is refactored. They provide documentation, ensure correctness, and maximize the ability to refactor!

However, there are a couple drawbacks to writing tests at this
1) Testing with usecases at the service level is more verbose than unit testing. There is significantly more "setup" to do since repos and other adapters are involved.
2) You will require investment in tooling in order to keep the tests readable with all of this setup.

To deal with these problems, I strongly recommend [MAKING A DSL FOR USECASES](make_a_dsl_for_usecases.md)