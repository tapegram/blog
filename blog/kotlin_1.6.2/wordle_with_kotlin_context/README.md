# Wordle with Kotlin Context

## Running the Wordle CLI
`./wordle`

## Kotlin 1.6.20
https://blog.jetbrains.com/kotlin/2022/02/kotlin-1-6-20-m1-released/#try-new-features-and-provide-feedback

Messing around with the prototype of the new context receivers.

Going to implement Wordle as a CLI because why not.

## Dependencies
I like using
- Arrow
- Kotest

Planning on experimenting with [Clikt.](https://ajalt.github.io/clikt/)

I really wanted to include [Arrow Analysis](https://arrow-kt.io/docs/meta/analysis/) to tighten up the type-safety in a few areas (see the definition for `String.toWord`, for example) but I was running into a bunch of compilation issues. I was too lazy to look into them further for a blog post, but I suspect they may have been related to using 1.6.20-M1.

## Thoughts on Context Receivers and other notes
First, they are great and I would like to use them in production ASAP. 

They allow you to enforce function purity with the compiler (as long as you actually agree as a team to use them for effects as a convention), similar to using the `suspend` modifier, but additionally allow granularity in specifying what specific effects the function will generate.

For example, compare these two functions:
```kotlin
suspend fun foo(name: String): String {
    // pretend this calls a function that calls a library that calls another function, etc.
    // we don't want to look into here unless we have to.
}
```

```kotlin
context(GreetingRepo)
fun foo(name: String): String {
    // pretend this calls a function that calls a library that calls another function, etc.
    // we don't want to look into here unless we have to.
}
```

Both of these functions can only be called in an "impure" context, but the latter lets you know what specific effects will be generated (and you can take a peak at the context definition at any time with a simple jump-to-definition).

Additionally, because contexts work with abstractions (or interfaces), they also double as a chance to push out "infrastructure" code somewhere else!

```kotlin
with(GreetingRepo.Postgres.Production) {
    foo("Bob")
}

with(GreetingRepo.Redis.Stage) {
    foo("Bob")
}

with(GreetingRepo.InMemory) {
    foo("Bob")
}
```

In your main function it might look something like this:
```kotlin
fun main(args: Array<String>) = 
    with(Profiles.Production) {
        program()
    }

fun main(args: Array<String>) =
    with(Profiles.Stage) {
        program()
    }

fun main(args: Array<String>) =
    with(Profiles.Local) {
        program()
    }

fun main(args: Array<String>) =
    with(Profiles.Stage) {
        program()
    }
```

(I personally am gravitating towards minimizing the amount of runtime/env lookups and instead deploying separate "apps" for each profile. If you go hardcore into ports and adapters or an effect handling system like described here, each new "app" will be a single file composing your "adapters/interpreters" with your usecases. You will still need env vars for things like secrets, but building applications like this has been a big improvement in my day-to-day work life).

Ideally, you can just wire up all of your "interpreters" or "handlers" for these contexts in one main function. This makes testing super easy, as well as swapping in different concrete implementations of data sources, adapters for APIs (maybe swapping in Stripe for Adyen), adding in different auth providers, etc. Really helpful stuff!

This is really similar ports and adapters but with the benefit of the compiler encouraging you the whole time and some other small ergonomic benefits! Hopefully, this means the compiler will guide junior engineers to using and respecting a good architecture, instead of needing hand-slapping and denying PRs, let the compiler be the bad guy! If you are interested in these ideas I'd recommend checking out my lightly attended talk on how [Discipline Fails!](https://www.youtube.com/watch?v=nEGIfvxK8Uo)

That all being said here are some thoughts that came up while messing around with this wordle app.

### `suspend` or `context`? (or both?)
I'm in the habit of trying to use `suspend` wherever reasonable to indicate that a function is impure, as [the Arrow team recommends.](https://arrow-kt.io/docs/fx/purity-and-referentially-transparent-functions/). That being said, with these new context receivers, you can get approximately the same value out of marking all impure functions with the context they need to run in.

So should we stick to using one over the other? Or both.. sometimes? All the time? I'm not sure yet. IMO, `context` feature is strictly better than `suspend` because it is more specific and it has the added benefit of doing some dependency injection-like behavior.

That being said, I think treating all impure functions as "async" also makes a lot of sense. I'm no expert, but the implementation for effect handlers and coroutines are actually really similar (passing around control of a program using continuations), so it might make sense to do both. Additionally, a lot of the Arrow libraries require suspended functions in order to do really useful thinks like `either` block computations.

So for now, I'm going to stick with doing both (where appropriate, see the section about escape hatches).

Additionally, I'm really excited to see how this new feature is incorporated into Arrow. I already see [Simon doing interesting stuff with it in his blog post.](https://nomisrev.github.io/context-receivers/)

### Ports and Adapters or Effect Systems?
To me, these patterns seem really similar, but with some clear wins for the effect system..

First, to compare how these might look side-by-side

```kotlin
// Effect System / Context Receivers
context(CreateWordleContext)
suspend fun createWordle(): Either<CreateWordleFailure, Wordle> = either {
    val wordleId = uuid()

    ensure(!doesWordleExist(wordleId).bind()) {
        CreateWordleFailure.WordleAlreadyExists(wordleId)
    }

    Wordle.new(
        id = wordleId,
        answer = getWordleWord(),
    )
        .save()
        .bind()
}
```

```kotlin
// Ports and Adapters
data class Service(
    val uuidGenerator: UUIDGenerator,
    val wordleRepository: WordleRepository,
) {
    suspend fun createWordle(): Either<CreateWordleFailure, Wordle> = either {
        val wordleId = uuidGenerator.uuid()

        ensure(!wordleRepository.doesWordleExist(wordleId).bind()) {
            CreateWordleFailure.WordleAlreadyExists(wordleId)
        }

        Wordle.new(
            id = wordleId,
            answer = getWordleWord(),
        )
            .save() // Calling wordleRepository is hidden in a private method
            .bind()
    }
}
```

These actually look really similar. The difference is that the "context" for the usecase is the service wrapper that has to be provided explicit implementations of uuid generator and wordle repository.

This explicitness may be something you want. Though in my opinion, this explicitness reads more like boilerplate and is also a bit non-intuitive to more junior engineers. This non-intuitiveness leads to the architecture being broken a lot when you aren't looking.

Both approaches work great, but I would lean towards using the context receivers since its a feature of the language instead of a "pattern" that must be learned outside of code an adhered to over time by exercising discipline. (Or at least, it requires less discipline to use features provided by the language than it does to introduce abstraction/indirection via design patterns.)

### Composability
My major complaint is that `with` only accepts a single argument, so you can not easily "compose" handlers/interpreters together. This is a bit of a minor complaint, but its the difference between
```kotlin
fun main(args: Array<String>) = with(context()) { program() }

fun context(): WordleContext = Context(
    wordles = mutableListOf(),
)

data class Context(val wordles: MutableList<Wordle>): WordleContext {
    override fun get(id: WordleId): Either<GetWordleFailure, Wordle?> =
        wordles.find { it.id == id }.right()

    override fun save(wordle: Wordle): Either<SaveWordleFailure, Unit> {
        wordles.removeIf { curr -> curr.id == wordle.id }
        wordles.add(wordle)
        return Unit.right()
    }

    override fun exists(id: WordleId): Either<WordleExistsFailure, Boolean> =
        (wordles.find { curr -> curr.id == id } != null).right()

    override fun uuid(): UUID = UUID.randomUUID()

    override fun getWordleWord(): Word =
        // Very lazy
        "CAKES".toWord()

    override fun isInDictionary(word: Word): Boolean =
        // Even lazier.
        // If this was real we would want to do real stuff with a DB or file or anything other than this.
        true
}
```

and something more reusable and ergonomic like
```kotlin
fun main(args: Array<String>) = 
    with(
        WordleRepo.InMemory(), 
        Dictionary.OnlyWordIsCakes, 
        UUIDGenerator.HardCoded(UUID.randomUUID)) { 
        program() 
    }
```

This would make it easier to reuse the same interpreters/handlers in different places and to mix and match more easily. Take a peak at some of the tests to see see how similar contexts involve a lot of reimplementation of the same behavior.

### Escape Hatches / Surface area for abuse
Unlike Haskell and Unison, which are pure functional languages, Kotlin does not force purity by default. It is up to you and your team to agree to flag impure functions appropriately. If you do this as a habit, you will get the benefit of the compiler enforcing function purity which naturally leads to ports and adapters-like architectures! However, it doesn't prevent people from just intentionally breaking the rules.

Let's say I have a pure function
```kotlin
fun pureFn(name: String) = "Hello, $name!"
```

But when you are on vacation someone on call had to fix a bug with some external state and they found that the easiest way to do that was just to add a side effect.
```kotlin
fun pureFn(name: String) {
    saveToDatabase(name)
    return "Hello, $name!"
}
```

If you already have all your DB APIs written using context receivers or suspended functions, the compiler will say "Hey! No can do!" and the dev will need to refactor to compose the effect with the pure function higher up in the code (probably in the service/usecase/application layer).

However, if this is a new piece of functionality that the junior dev wrote, or something old that hasn't been updated yet, the compiler won't catch it, and now you have this extra tech debt to deal with, and the longer it sits there the more entrenched it becomes and the debt starts to compound.

Haskell and Unison don't have that problem since they enforce purity every all the time, in Kotlin we have to opt in to it.

That being said, there are some benefits to having escape hatches. In particular: Logging and Observability.

I don't particularly care if the impurity of the function comes from logging. If I can get away with it, I'm totally cool with just closing my eyes and saying "Yup, this is pure!"
```kotlin
fun pureFn(name: String) {
    log("pureFn called with $name")
    return "Hello, $name!"
}
```

Though I would also argue for finding away to hide observability from "business logic," usually with [annotations or decorators.](https://www.baeldung.com/java-jcabi-aspects)

```kotlin
@Loggable
fun pureFn(name: String) {
    return "Hello, $name!"
}
```

Is this pure? Technically not, I guess. But I really don't want to be bothered with adding a context or suspending the function for it. So having this escape hatch can be helpful in some cases! It's the tradeoff for requiring additional engineering discipline to keep the arch scalable.

### Incompatibility of Arrow Analysis (or maybe I'm just doing it wrong)
I really wanted to add Arrow Analysis to add some type-safety to some code that normally feels a little painful to make safe. In particular
```kotlin
fun String.toWord(): Word =
    // TODO: Add arrow analysis to force this to always be a 5 char string and always caps.
    // Right now this "function" is lying in its signature.
    Word(
        char1 = this[0].uppercaseChar(),
        char2 = this[1].uppercaseChar(),
    char3 = this[2].uppercaseChar(),
        char4 = this[3].uppercaseChar(),
        char5 = this[4].uppercaseChar(),
    )
```

This function fails if the string has less than 5 chars, it also only takes the first 5 chars of a longer string which isn't intuitive. And beyond that it takes any char for each slow in the word. That includes punctuation, numbers, etc.

But doing something like
```kotlin
fun String.toWord(): Either<ToWordFailure, Word> =
    Word.fromString(this)

data class Word private constructor(val char1: Char, val char2: Char, val char3: Char, val char4: Char, val char5: Char) {
    companion object {
        fun fromString(string: String): Either<ToWordFailure, Word> {
            if (string.size < 5) {
                return Left(ToWordFailure.NotEnoughChars(string.length))
            } 
            if (string.size > 5) {
                return Left(ToWordFailure.TooManyChars(string.length))
            } 
            if (string.containsNonAlpha()) {
                return Left(ToWordFailure.NonAlpha(string))
            }
            return Word(
                char1 = string[0].uppercaseChar(),
                char2 = string[1].uppercaseChar(),
                char3 = string[2].uppercaseChar(),
                char4 = string[3].uppercaseChar(),
                char5 = string[4].uppercaseChar(),
            ).right()
        }
    }
}
```
feels kinda bad. This is exactly the thing that arrow analysis would clean up and put the validation at compile time and with much better devEx and ergonomics. I love the idea of `"CAKES".toWord()` still working fine and not returning an either, and `"BAKING".toWord()` throwing a compiler error. That means we can keep writing tests like this
```kotlin
class CantGuessMoreAfterGettingAnswerTest : StringSpec({
    "Cant guess more after getting the answer" {
        with(DummyGuessContext(mutableListOf(Cakes.wordle))) {
            guess(Cakes.wordle.id, "CAKES".toWord())
            guess(Cakes.wordle.id, "CRATE".toWord()) shouldBeLeft GuessWordFailure.GameIsOver(Cakes.wordle.id)
        }
    }
})
```

Unfortunately, I couldn't get the plugin to work. It could be my fault, but I have a small suspicion it's just incompatible with this M1 release.

I'm far too lazy to spend additional time looking into it for this blog post though, so here is yet another example of "discipline failing" and me just leaving in a function with a lying type signature.

### Some design Decisions
Not really related to context receivers, but I did apply a good chunk of the patterns mentioned in a [previous blog post and tests and architecture](https://github.com/tapegram/blog/tree/main/blog/an_opinionated_set_of_testing_patterns). You may notice I didn't apply ALL OF THEM, however. In particular, I am leaking domain objects everywhere in tests and outside the usecases, and I did not create a DSL for testing usecases. I made the decision not to do those things simply because those are great patterns when you have to refactor and maintain this code over time. And I have no intention of touching this code again after this post, hahaha.

However, I seem pathologically unable not write some kind of testing tooling to make writing tests easy, nice, and self-documenting. So I only tested the usecases (application layer), and I made a stable of "realistic" wordles and guesses to make writing tests easy (without all the boilerplate that comes with the semi-typesafe objects like `Word` and `Guess` which are wrappers around strings.)
