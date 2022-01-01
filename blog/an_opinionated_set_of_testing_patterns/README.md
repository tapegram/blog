# An Opinionated Set of Testing Patterns

My personal experience learning software architecture grew out of Test Driven Development. As a result, I strongly feel that one of the best ways to "naturally" learn how to write "clean code" and to design "clean architectures" is to focus on writing tests. Not just any tests though: Good Tests.

But what is a Good Test? Like nearly every question, the answer is "it depends." Most of my learning and opinions are shaped by growing up in an e-commerce engineering organization where the most critical source of problems was [compounding business complexity and not necessarily the volume of data or more technology-focused demands.](https://youtu.be/avi-TZI9t2I?t=136)

That being said, a Good Test should 

1) Ensure correctness
2) Ensure refactorability

These are both essential qualities. That is, they are equally important, and your tests should never compromise on either. In my experience, most engineers focus on just the correctness aspect of tests and completely neglect the refactorability of tested code. Writing tests this way will not lead to the natural development of good architecture. [Even worse, it tends to actively hurt the system, team, and organization over time.](https://www.youtube.com/watch?v=xPL84vvLwXA&ab_channel=VMwareTanzu)

The reason this happens is that *tests introduce coupling.* 

## Tests and Coupling
When you write a test, that test is directly dependent on the shape of the function it calls. And not just that, but tests are coupled to the functions used for setup and teardown. This dependency makes it harder to refactor.

```kotlin
data class Greeting(val language: String, val name: String) {
  fun sayHello(): String = when (language) {
    "english" -> "Hello, $name"
    "french" -> "Bonjour, $name"
    "british" -> "'Ello, govnuh"
    else -> "Hey there, $name"
  }
}

class GreetingTest : StringSpec({
    "greet in english" {
        val greeting = Greeting("english", "Bob")
        greeting.sayHello() `shouldBe` "Hello, Bob"
    }

    "greet in french" {
        val greeting = Greeting("french", "Pierre")
        greeting.sayHello() `shouldBe` "Bonjour, Pierre"
    }

    "greet in british" {
        val greeting = Greeting("british", "Tobias")
        greeting.sayHello() `shouldBe` "'Ello, govnuh"
    }
    
    "greet someone with an unknown language" {
        val greeting = Greeting("quebecois", "Esmerelda")
        greeting.sayHello() `shouldBe` "Hey there, Esmerelda"
    }
})
```

Seems ok so far, right? But these tests only care about *Correctness* and not at all about *Refactorability*.

I know this is a contrived example, but let's pretend we have high standards for our Hello World code, standards that match more complicated domains that don't translate well to blog posts like e-commerce or healthcare.

For starters, let's stop passing around raw strings. This code is crying out for an enum.

```kotlin
enum class Dialect {
  English,
  French,
  British,
  Unknown,
}

data class Greeting(val dialect: Dialect, val name: String) {
  fun sayHello(): String = when (dialect) {
    English -> "Hello, $name"
    French -> "Bonjour, $name"
    British -> "'Ello, govnuh"
    Unknown -> "Hey there, $name"
  }
}
```

But of course, now every test from before is broken. So we have to update all of them again. In real life, this is dozens or hundreds of tests depending on how critical the code you are refactoring is. The more critical the code is, the more important it is to continuously refactor it. Yet, the more critical the code is, the more tests you will have, and thus refactoring is even more difficult.

```kotlin
class GreetingTest : StringSpec({
    "greet in english" {
        val greeting = Greeting(English, "Bob")
        greeting.sayHello() `shouldBe` "Hello, Bob"
    }

    "greet in french" {
        val greeting = Greeting(French, "Pierre")
        greeting.sayHello() `shouldBe` "Bonjour, Pierre"
    }

    "greet in british" {
        val greeting = Greeting(British, "Tobias")
        greeting.sayHello() `shouldBe` "'Ello, govnuh"
    }
    
    "greet someone with an unknown language" {
        val greeting = Greeting(Unknown, "Esmerelda")
        greeting.sayHello() `shouldBe` "Hey there, Esmerelda"
    }
})
```

Note that we had to update every test even though the "business logic" stayed the same! What a waste of effort!

Let's do another one. Say, I want to embrace OOP and types. We can refactor the greeting to be a sealed class.

```kotlin
sealed class Greeting {
  abstract fun sayHello(): String

  data class English(val name: String) {
    override fun sayHello() = "Hello, $name"
  } : Greeting()

  data class French(val name: String) {
    override fun sayHello() = "Bonjour, $name"
  } : Greeting()

  object British {
    override fun sayHello() = "'Ello, govnuh"
  } : Greeting()

  data class Unknown(val name: String) {
    override fun sayHello() = "Hey there, $name"
  } : Greeting()
}

class GreetingTest : StringSpec({
    "greet in english" {
        val greeting = Greeting.English("Bob")
        greeting.sayHello() `shouldBe` "Hello, Bob"
    }

    "greet in french" {
        val greeting = Greeting.French("Pierre")
        greeting.sayHello() `shouldBe` "Bonjour, Pierre"
    }

    "greet in british" {
        val greeting = Greeting.British
        greeting.sayHello() `shouldBe` "'Ello, govnuh"
    }
    
    "greet someone with an unknown language" {
        val greeting = Greeting.Unknown("Esmerelda")
        greeting.sayHello() `shouldBe` "Hey there, Esmerelda"
    }
})
```

Again, we had to change all the tests. This is already getting annoying, even for this trivial hello world example with four test cases. This coupling discourages refactoring and calcifies the code.

[Uncle Bob has a great blog post exploring this topic (among others)](https://blog.cleancoder.com/uncle-bob/2017/03/03/TDD-Harms-Architecture.html)

```
Still another common argument is that as the number of tests grows, a single change to the production code can cause hundreds of tests to require corresponding changes. For example, if you add an argument to a method, every test that calls that method must be changed to add the new argument. This is known as The Fragile Test Problem.

A related argument is: The more tests you have, the harder it is to change the production code; because so many tests can break and require repair. Thus, tests make the production code rigid.

...

Yes. That’s right. Tests need to be designed. Principles of design apply to tests just as much as they apply to regular code. Tests are part of the system; and they must be maintained to the same standards as any other part of the system.

...

The problem is – and I want you to think carefully about this next statement – a one-to-one correspondence implies extremely tight coupling.

Think of it! If the structure of the tests follows the structure of the production code, then the tests are inextricably coupled to the production code

...

What makes TDD work? You do. Following the three laws provides no guarantee. The three laws are a discipline, not a solution. It is you, the programmer, who makes TDD work. And you make it work by understanding that tests are part of the system, that tests must be designed, and that test code evolves towards ever greater specificity, while production code evolves towards ever greater generality.

Can TDD harm your design and architecture? Yes! If you don’t employ design principles to evolve your production code, if you don’t evolve the tests and code in opposite directions, if you don’t treat the tests as part of your system, if you don’t think about decoupling, separation and isolation, you will damage your design and architecture – TDD or no TDD.
```

## Patterns!
Back to the point of this post, if you practice TDD and try to write tests that ensure correctness and refactorability, you will naturally learn to build scalable and testable architectures. The following are a collection of patterns I use day to day when writing code, and that have held up well over time. By starting with TEST DRIVEN DEVELOPMENT, I believe a junior engineer with some curiosity and initiative, will eventually discover some form of all of these patterns.

- [TEST DRIVEN DEVELOPMENT](../patterns/test_driven_development.md)
- [PORTS AND ADAPTERS](../patterns/ports_and_adapters.md)
- [ENFORCE REFERENTIAL TRANSPARENCY](../patterns/enforce_referential_transparency.md)
- [HIDE THE DOMAIN](../patterns/hide_the_domain.md)
- [KEEP THE DOMAIN PURE](../patterns/keep_the_domain_pure.md)
- [MODEL DOMAIN ERRORS](../patterns/model_domain_errors.md)
- [TEST USECASES](../patterns/test_usecases.md)
- [MAKE A DSL FOR USECASES](../patterns/make_a_dsl_for_usecases.md)


## An example app
One of my favorite katas is the [Birthday Greeting Kata](http://matteo.vaccari.name/blog/archives/154.html) which is used to learn TEST DRVIEN DEVELOPMENT and PORTS AND ADAPTERS.

I've written [another blog post about this kata for the Unison blog](https://www.unisonweb.org/2021/08/20/birthday-kata/) as well, in case people are interested in algebraic effects (hint:you should be). Maybe we will be able to get something good in [Kotlin soon](https://github.com/arrow-kt/arrow/issues/2556)!

Anyway, I've gone ahead an done a quick run through of the Birthday Greeting Kata and tried to apply the above patterns. There is always room to grow and things that I would change, but I think this is a decent example for getting a sense of what all this looks like together.

Please note that there might not be enough complexity in this domain to necessarily warrant all of the abstraction I have added. I have some difficulty providing actually complex domains (like you would have at work) in a blog post or talk just because they are complex and they either take a long time to write up or don't fit into a single talk.

I intend to eventually do another blog post with a more complex domain that I have some familiarity with so I can show PR by PR what decisions are made and how the testing infrastructure helps support an evolving architecture.

Take a look! The the code is [here](https://github.com/tapegram/blog/tree/main/blog/an_opinionated_set_of_testing_patterns/birthday_kata)

If you want to see an example of a rather large refactor (generalizing the messaging instead of only allowing emails), take a look at [this draft pr.](https://github.com/tapegram/blog/pull/2)

## Closing Thoughts
In some ways, all of these ideas come just from applying the same principles we apply to our production code to our tests. SOLID, in particular, comes to mind. I would go so far as to say it is *more* important for your tests to be written and abstracted in production quality than the actual production code itself since the production code is derived and maintained through the tests.