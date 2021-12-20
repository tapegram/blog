# TEST DRIVEN DEVELOPMENT

## Problem Context
Test Driven Development is pretty much always applicable when writing code. In that regard, you may choose to call it a "top level pattern" where the problem context is simply "creating software."

In particular, the values and principles that inspired TDD go hand in hand with Agile and XP.

## Application
At its heart, TDD is a discipline wherein an engineer chooses to write tests before writing production code, instead of writing code and then tests.

However, in my opinion, and in the general consensus of the community, the favored process is the following:

```
1. Add a test

The adding of a new feature begins by writing a test that passes iff the feature's specifications are met. The developer can discover these specifications by asking about use cases and user stories. A key benefit of test-driven development is that it makes the developer focus on requirements before writing code. This is in contrast with the usual practice, where unit tests are only written after code.

2. Run all tests. The new test should fail for expected reasons

This shows that new code is actually needed for the desired feature. It validates that the test harness is working correctly. It rules out the possibility that the new test is flawed and will always pass.

3. Write the simplest code that passes the new test

Inelegant or hard code is acceptable, as long as it passes the test. The code will be honed anyway in Step 5. No code should be added beyond the tested functionality.

4. All tests should now pass

If any fail, the new code must be revised until they pass. This ensures the new code meets the test requirements and does not break existing features.

5. Refactor as needed, using tests after each refactor to ensure that functionality is preserved

Code is refactored for readability and maintainability. In particular, hard-coded test data should be removed. Running the test suite after each refactor helps ensure that no existing functionality is broken.
Examples of refactoring:
- moving code to where it most logically belongs
- removing duplicate code
- making names self-documenting
- splitting methods into smaller pieces
- re-arranging inheritance hierarchies

6. Repeat
The cycle above is repeated for each new piece of functionality. Tests should be small and incremental, and commits made often. That way, if new code fails some tests, the programmer can simply undo or revert rather than debug excessively. When using external libraries, it is important not to write tests that are so small as to effectively test merely the library itself,[4] unless there is some reason to believe that the library is buggy or not feature-rich enough to serve all the needs of the software under development.
```
Taken from [the wikipedia article on TDD.](https://en.wikipedia.org/wiki/Test-driven_development)

Here is [an excellent demo of Uncle Bob's "Three Laws of TDD" by the man himself.](https://www.youtube.com/watch?v=qkblc5WRn-U&ab_channel=IntelliJIDEAbyJetBrains)

## Resulting Context
The immediate benefits of embracing TDD include

1) Fast feedback loops in the dev-cycle - You know instantly if your code is working or not as you go, instead of spending an hour writing a huge block of code and then only finding out after that *something* about it doesn't work.
2) Continuous Validation - As mentioned above, every change is small and incremental, so when something breaks you know exactly what it was that broke. [No need for debuggers or spending hours looking for the problem. Just hit "undo."](https://www.artima.com/weblogs/viewpost.jsp?thread=23476). And for a personal experience: I have not needed to use anything beyond tests and the occasional print statements in the last 5ish years (since I started practicing TDD).
3) Writing tests firsts encourages writing testable code - This is one of the biggest wins of TDD since it naturally pushes you towards a Ports and Adapters architecture, as well as writing testable pure code.
4) Encourages Incremental Work - After every "cycle" in TDD, you can decided "Is this good enough for a PR?" Similarly, you are done with the ticket when all of your AC have corresponding passing tests. EZ-PZ.
5) Focuses Discussion on the Problem - Because you start with tests first, you need to know what "specs" you are providing for the system. In other words, writing tests helps you understand the "usecases" or "business logic" you need to implement, before you actually implement them. I workshop test cases with product all the time and require that Acceptance Criteria on tickets be written in given / when / then style, when possible.
6) TDD gives you, the engineer, tons of confidence in the code you write. It may come across as a bit arrogant, but one my code gets deployed and someone reports an issue, I can sit back smugly knowing it wasn't my commit (unless there was a miscommunication about business rules, but that's really rare in my experience).

However, there are quite a few traps you can fall into by misapplying TDD, as well as challenges to overcome to get the above value out if it.

See [this talk](https://www.youtube.com/watch?v=xPL84vvLwXA&ab_channel=VMwareTanzu) for a great crash course on what doing TDD wrong looks like. In summary: TDD will fail if you don't ensure that you write tests that promote refactorability.

The first problem you may run into is just that you are often not writing code from scratch -- instead you are working with legacy code that is inherently difficult to test. Maybe the code is full of effects that require mocking (which then leads to difficulty refactoring), or the code isn't well abstracted or isolated, so testing any bit requires testing the whole dang system. Running into these difficulties should re-affirm the value of TDD, since you can now plainly see and feel the problems that happen when testing isn't considered part of the final production code.

There isn't an easy fix for this. You have to write tests at the highest level possible to allow for testing, mock/patch as little as possible to get the tests working, and refactor refactor refactor. And also stop writing code that isn't testable, factor out code when possible so you can practice TDD on that small bit, and then grow it. [This talk](https://www.youtube.com/watch?v=8bZh5LMaSmE&t=830s&ab_channel=Confreaks) by Sandi Metz may be helpful if you are stuck in this position!

Another problem is the interesting idea of *emergent* architectures vs *designed* architectures. Some TDD purests would claim that you start from scratch wtih TDD and through refactoring the architecture emerges. Others [claim that TDD destroys the ability to create thoughtfully designed abstraction matching the mental model of your users](https://www.youtube.com/watch?v=ZrBQmIDdls4). I think this is a very interesteding discussion and have personally landed on an approach [resembling what is described here](https://www.youtube.com/watch?v=KtHQGs3zFAM), but with a little DDD mixed in: Scaffold your initial domain models, create the aggregates you have a very high confidence are the right ones for your system (it's not as important if the internals are "right"), wrap them in a service, and then go to town with TDD against the service.

A final note on "Mastery." TDD is a discipline and a practice aligned with our values when producing software. However, as you practice it and gain mastery, I have found you begin to write the kind of product code you would write with TDD, without writing the tests first. It becomes a part of who you are as an engineer. In my experience, code just feels "wrong" if it's not easy to test. I'm not as "hardcore" about always writing all the tests according to the three laws as I have been in years past, but I'm still constantly thinking about testability and working in clean, green increaments. [Of course, I mostly write tests first in a TDD style, but I feel the real "value" of it has been its effect on how I think about production code.](https://blog.cleancoder.com/uncle-bob/2016/11/10/TDD-Doesnt-work.html)