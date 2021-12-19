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
Fast feedback loops in the dev cycle.

Continuous validation when writing code (easy debugging).

Encourages writing "testable code." What is "testable code."

Focusing discussions on usecases / AC.

Encourages incremental work

Build tons of confidence in quality (because its better)



Must value refactorability or TDD can lead to future difficulties when extending or refactoring.

Design vs Emergent Architectures

Mastery (Uncle bob blog post)

Relationship with Types