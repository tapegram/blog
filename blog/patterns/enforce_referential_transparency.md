# ENFORCE REFERENTIAL TRANSPARENCY

## Problem Context
You want to apply [PORTS AND ADAPTERS](ports_and_adapters.md) at scale in your work but people keep breaking the architecture. Either they are well-intentioned junior engineers who don't understand the architecture and think they are helping, or they are seniors on a time crunch who promise to "address the tech debt later" (this will never happen). And once the coupling is introduced it tends to get worse instead of better.

Or, you want to [KEEP THE DOMAIN PURE](keep_the_domain_pure.md) but people keep importing the ORM and doing http requests inside the domain objects!

This probably puts you in a painful spot where you have to slap hands, reject PRs, and generally behave like the code police. This is not good for anyone, especially when an excited junior engineer shows you the PR they spent a week on that nails all the acceptance criteria, and you have to reject it and demand they redo everything because they haven't read all the books on architecture you have. It breaks their little hearts.

## Application
This exact problem is discussed at length in [this excellent talk](https://www.youtube.com/watch?v=US8QG9I1XW0).

The pitch is basically to use a tool or language that removes the burden of enforcing the architecture from you (or any other human) and has the computer naturally enforce it. By making "doing the wrong thing" harder than "doing the right thing" no one needs to read a big book or demonstrate an iron will in the face of urgent deadlines -- it just happens!

In our case, it's been well observed the PORTS AND ADAPTERS happens more or less naturally in pure languages like Haskell, where the compiler won't let a pure function call an impure one. So if you have the option, use Haskell.

I work at a company that does not use Haskell though, but luckily we do use a lot of Kotlin, and you can do something similar in Kotlin by adopting the convention that all impure functions should be marked with a `suspend` modifier.

This requires buy-in from your team, but in my experience, everyone wants to "do the right thing" and can be convinced this is worth adopting. Then later, when discipline fails or ignorance reigns, the compiler will yell at them when they try to call talk to the DB from the domain. If they look for the easiest possible answer, they will end up composing pure and impure code the service, which is more or less what we want! If the domain can't call any impure code, then it can't depend on the infrastructure. Naturally then, one has to write code where the impure infrastructure code depends on the pure domain model and oops we've invented PORTS AND ADAPTERS again.

For further documentation, please see [this excellent write-up by the Arrow team.](https://arrow-kt.io/docs/fx/purity-and-referentially-transparent-functions/)

Unfortunately, you can enforce this in languages where asynchronous or effectful behavior is part of the type signature of a function. For instance, you may be able to do something similar in JS by labeling impure functions as `async`. However, I'm not aware of a good way to do this in a language like Python. For languages that don't support this pattern, you will have to rely on culture, PR reviews, and education to encourage separation of pure/impure functions, possibly adopting a function naming convention to identify impurity. Ruby, for example, [has a convention of marking effectful functions with a `!` in the name.](https://hackernoon.com/functional-programming-in-ruby-how-to-create-pure-functions-0uq3ujr) I haven't worked a lot with custom linting rules, but you could theoretically update a linter to disallow calling `!`'d (or whatever indicates impurity in your convention) from pure functions.

## Resulting Context
Now you are naturally guiding your whole team towards PORTS AND ADAPTERS and KEEP THE DOMAIN PURE! It's a huge win for maintainability and scalability of the code without needing to play the villain!

There are, of course, some downsides.

If you are using Kotlin, and this is by convention, you will need to make sure that the team continues to introduce new impure code with `suspend` and refactor legacy code to use `suspend`. You get this by default with a language like Haskell. However, one of the benefits of doing this by convention is that you can agree to except some impure code, like logging. Being allowed to log in and around pure functions is greatly helpful (and pure functions are so good at being logged, just get the input and the output! You can even use a cool library like [jcabi-aspects](https://www.baeldung.com/java-jcabi-aspects#loggable)).

Additionally, this pattern pairs well with [MODEL DOMAIN ERRORS](model_domain_errors.md) since one of the larger sources of impurity is thrown exceptions. If you can eliminate a lot of them, you will get a lot more pure code!