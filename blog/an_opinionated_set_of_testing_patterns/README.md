# An Opinionated Set of Testing Patterns

My personal experience learning software architecture grew out of Test Driven Development. As a result, I strongly feel that one of the best ways to "naturally" learn how to write "clean code" and to design "clean architectures" is to focus on writing tests. Not just any tests though: Good Tests.

But what is a Good Test? Like nearly every question, the answer is "it depends." Most of my learning and opinions are shaped by growing up in an e-commerce engineering organization where the most critical source of problems was [compounding business complexity and not necessarily the volume of data or more technology-focused demands.](https://youtu.be/avi-TZI9t2I?t=136)

That being said, a Good Test should 

1) Ensure correctness
2) Ensure refactorability

These are both essential qualities. That is, they are equally important, and your tests should never compromise on either. In my experience, most engineers focus on just the correctness aspect of tests and completely neglect the refactorability of tested code. Writing tests this way will not lead to the natural development of good architecture. (Even worse, it tends to actively hurt the system, team, and organization over time.)[https://www.youtube.com/watch?v=xPL84vvLwXA&ab_channel=VMwareTanzu]

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

## Patterns!
Back to the point of this post, if you practice TDD and try to write tests that ensure correctness and refactorability, you will naturally learn to build scalable and testable architectures. The following are a collection of patterns I use day to day when writing code, and that have held up well over time.

- Test Driven Development
- Keep the Domain Pure
- Test Abstractions Not Concretions
- Ports and Adapters Architecture
- TODO: what else?

## Closing Thoughts
In some ways, all of these ideas come just from applying the same principles we apply to our production code to our tests. SOLID, in particular, comes to mind. I would go so far as to say it is *more* important for your tests to be written and abstracted in production quality than the actual production code itself, since the production code is derived and maintained through the tests.