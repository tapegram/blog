# KEEP THE DOMAIN PURE

## Problem Context
If you are practicing [TEST DRIVEN DEVELOPMENT](test_driven_development.md) you will have quickly run into the issue that it is really hard to test impure code. Most impure code tends to come from infrastructure, so you may have already applied [PORTS AND ADAPTERS](ports_and_adapters.md) to try to separate out most of the impurity and then [TEST WITH IN MEMORY ADAPTERS](test_with_in_memory_adapters.md) and [HIDE THE DOMAIN](hide_the_domain.md) to make testing as easy as possible.

Regardless, it can be really hard to test and reason about your business logic if it is impure.

A simple example:

```kotlin
data class Ticket(
  val id: UUID,
  val description: Description,
  val assigned: UUID?,
  val createdOn: Timestamp,
  val modifiedOn: Timestamp,
)

data class TicketService(val ticketRepo: TicketRepo) {
  fun create(description: String, assignedUserId: UUID?): TicketResponse =
    Ticket(
      id = UUID.randomUUID(),
      description = Description(description),
      assigned = assignedUserId,
      createdOn = Timestamp.now(),
      modifiedOn = Timestamp.now(),
    )
    .also { ticketRepo.save(it) }
    .toResponse()
}
```

If you were to write tests for this, do you have to patch or mock `Timestamp.now()` or `UUID.randomUUID()`? Or do you just not assert about them at all?

You can probably get away with ignoring the id, but timestamps often affect business logic.

What if you wanted to fetch every ticket that is older than X and hasn't been modified in Y? That starts to get a little gross, and patching is hard coupling to implementation details (which function is fetching the timestamp? what file is it used in?). Also patching tends to clutter the test and make it harder to reason about.

This is, of course, a very simple albeit common example. What about more complicated use cases like making calls to remote services? It just gets harder to test the domain when effects get involved.

## Application
Just pass in the result of the effect to the domain! Let's rely on the service composing pure and impure code to give us maximum control.

If you look at the above example, you can see we are already doing something like that with repositories which are another example of possibly impure code.

You have a couple options for how to do this. You can take the PORTS AND ADAPTERS approach and provide the service with an interface it can use to perform an effect.

```kotlin
interface UUIDGenerator {
  fun random(): UUID
}

interface Clock {
  fun now(): Timestamp
}

data class TicketService(
  val ticketRepo: TicketRepo,
  val uuidGenerator: UUIDGenerator,
  val clock: Clock
) {
  fun create(description: String, assignedUserId: UUID?): TicketResponse =
    Ticket(
      id = uuidGenerator.random(),
      description = Description(description),
      assigned = assignedUserId,
      createdOn = clock.now(),
      modifiedOn = clock.now(),
    )
    .also { ticketRepo.save(it) }
    .toResponse()
}
```

Now you can test by providing stubbed or mocked versions of those interfaces in plain ol' kotlin.

```kotlin
data class MockUUID(uuid: UUID): UUIDGenerator {
  override fun random() = uuid
}

data class BrokenClock(timestamp: Timestamp): Clock {
  override fun now() = timestamp
}

class CartTest : StringSpec({
    "create a ticket" {
      val uuid = UUID.randomUUID()
      val now = Timestamp.now()
      val result = TicketService(
        InMemoryTicketRepo(emptyList()),
        MockUUID(uuid),
        BrokenClock(now),
      ).create("description for the ticket", null) `shouldBe` TicketResponse(
        id = uuid,
        description = "description for the ticket",
        assigned = null,
        createdOn = now,
        modifiedOn = now,
      )
    }
})
```

Alternatively, you could choose to just require that some adapter somewhere provide this information for you.

```kotlin
data class TicketService(
  val ticketRepo: TicketRepo,
) {
  fun create(id: UUID, description: String, assignedUserId: UUID?, now: Timestamp): TicketResponse =
    Ticket(
      id = id,
      description = Description(description),
      assigned = assignedUserId,
      createdOn = now,
      modifiedOn = now,
    )
    .also { ticketRepo.save(it) }
    .toResponse()
}
```

```kotlin
class CartTest : StringSpec({
    "create a ticket" {
      val uuid = UUID.randomUUID()
      val now = Timestamp.now()
      val result = TicketService(
        InMemoryTicketRepo(emptyList()),
      ).create(id, "description for the ticket", null, now) `shouldBe` TicketResponse(
        id = uuid,
        description = "description for the ticket",
        assigned = null,
        createdOn = now,
        modifiedOn = now,
      )
    }
})
```

This is essentially the same idea proposed in this great talk about [dependency rejection](https://www.youtube.com/watch?v=cxs7oLGrxQ4).

## Resulting Context
The result of applying this pattern is a more scalable codebase. Keeping business logic pure and focused on "decisions" makes it easier to test and reason about. There is much less clutter on how or where the information driving the decision is coming from, you just codified the business logic.

Composing pure and impure code at the service level synergizes well with other patterns such as HIDE THE DOMAIN, PORTS AND ADAPTERS, and just in general makes life better! Please do not take this benefit lightly just because there is a simple example above!

Additionally, just trying to keep the domain pure can naturally lead to PORTS AND ADAPTERS, as other engineers will stumble upon similar patters in order to get a working solution that priortizes the purity of the domain.

However, sometimes it can be hard to maintain this (and other patterns) across a team, as not all engineers know about these pattern and [its a constant battle to maintain this architecture.](https://www.youtube.com/watch?v=US8QG9I1XW0&ab_channel=NDCConferences) If you can find a way to [ENFORCE REFERENCIAL TRANSPARENCY](enforce_referential_transparency.md) you will be setting up your team to apply these patterns by default, without intervention from more senior engineers.