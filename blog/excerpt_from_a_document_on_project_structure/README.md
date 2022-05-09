# Excerpt From a Document on Project Structure
While working on an internal project at Peloton to build out a "service template" I decided to write up some documentation describing the decision making that went into this template so that future engineers could get some context on why it was set up the way it was. More importantly, they could use this context to decide what they wanted to keep, and what it made sense to change.

Overall, I think it's a good intro to the kinds of discussions I'm interested in, including Pits of Success and Conway's Law.

I've excised the bits from the document that were peloton specific on directory layout and just kept in the meatier decision points.

# Project Structure
## Motivation
The template provided is structured based on a few core principles

1) Ports and Adapters - This is an architectural pattern where we isolate the "core" of an application from the "infrastructure." This is done via the concepts of "ports" (read: interfaces) and "adapters" (read: implementation of interfaces).
2) Pits of Success - One of the goals of this template is to provide an default architecture that encourages the use of patterns that Peloton considers "good" without necessarily requiring that every engineer have an expert knowledge of those patterns.
3) DevOps - We wanted to colocate as much of the infrastructure that is reasonable for an application developer to have ownership over such as the db schema and the kubernetes manifest. As we move towards microservice and more advanced technical patterns, we are trading the need for developer discipline and logical complexity for technical complexity, in the hope we can provide tools, education, and frameworks to solve or minimize that technical compexity.

The principles and patterns all deserve their own detailed discussions and have their own tradeoffs. In this default template structure, we have picked some application of them that we feel delivers the most value to our teams while still having the flexibility to be adapted to other use cases. These tradeoffs will be discussed in this doc and the linked docs about the above principles.

## Multimodule Projects
Here is an excellent quote from [Continuous Delivery](https://martinfowler.com/books/continuousDelivery.html)

> What is a component? This is a horribly overloaded term in software, so we will try to make it as clear as possible what we mean by it. When we talk about components, we mean a reasonably large-scale code structure within an application, with a well-defined API, that could potentially be swapped out for another implementation. A component-based software system is distinguished by the fact that the codebase is divided into discrete pieces that provide behavior through well-defined, limited interactions with other components. The antithesis of a component-based system is a monolithic system with no clear boundaries or separation of concerns between elements responsible for different tasks. Monolithic systems typically have poor encapsulation, and tight coupling between logically independent structures breaks the Law of Demeter.
>
> ...
>
>Employing a component-based design is often described as encouraging reuse and good architectural properties such as loose coupling. This is true, but it also has another important benefit: It is one of the most efficient ways for large teams of developers to collaborate.
>
>...
>
>Many projects are fine with a single version control repository and a simple deployment pipeline. However, many projects have evolved into an unmaintainable morass of code because nobody made the decision to create discrete components when it was cheap to do so. The point at which small projects change into larger ones is fluid and will sneak up on you.
>
>...
>
>Finally, it’s worth noting Conway’s Law, which states that “organizations which design systems . . . are constrained to produce designs which are copies of the communication structures of these organizations.” So, for example, open source projects where developers communicate only by email tend to be very modular with few interfaces. A product developed by a small, colocated team will tend to be tightly coupled and not modular. Be careful of how you set up your development team—it will affect the architecture of your application.

This quote touches on a lot of concepts we are trying to balance with this project structure, driven by thoughts about Conway's Law, discipline, boundaries, as well as more technical concerns.

### Leaving the Monolith
Peloton has outgrown the Monolith. Monolith's are a great architectural pattern for early, rapid work where scalability takes a back seat to building now just to stay alive or bring a product to market. While still growing rapidly, our primary constraint as an organization now is the scalability of our platform, not just how quickly we can crank out code. This is where a Monolith becomes less appropriate. The Monolith provides no boundaries and relies entirely on developer discipline to construct and maintain those boundaries. Python doesn't help much either. When the business asks for a feature, developers are always going to feel the pressure to deliver sooner rather than later, even at the expense of introducing coupling between otherwise unrelated parts of the codebase and as a result, between teams; at the expense of techdebt that will likely be deprioritized when the next critical feature request comes in -- even when, as is the case on Ecommerce, engineers and leadership encourage developers to push back on deadlines and deliver the "right" solutions. It's just not how teams and individuals think: they want to deliver the feature, they don't want to be the bad guy and say "sure, we could do this and ruin our architecture, but we have to say no and push back the delivery date by several days/weeks/months." And as the boundaries fail and coupling increases, it becomes increasingly difficult to uncouple the existing code, and increasingly difficult to add new features and make changes. It requires more teams to agree to change something that should be the purview of a single team, there is more risk, it is harder to test, etc. There is a natural entropy, and scaling is made increasingly difficult.

The lesson is: one cannot rely on engineer discipline to enforce boundaries. In order to scale and "go fast forever" we must enforce boundaries through technical means, at the expense of more (solvable) technical complexity.

Microservices are one of the hardest boundaries you can create. In the Monolith, you can be easily tempted to tightly couple your SalesOrder package to your Payment package by importing a low level function from one to the other. If these were microservices, that's not possible, you have to communicate via contract, and it is now "easier" to do the right thing, by either working off the contract, or, if necessary, updating the contract which is always "public" and leaks much less implementation detail. Of course it is still possible to couple services, for instance, with many synchronous API calls between them, but these are solvable by introducing tools and patterns for asynchronous messaging and other patterns.

### Monorepo vs Repo per Service
Given that we are embracing Microservices, we now have to decide where those service definitions live. Do we take a Monorepo approach where we put many independently deployable services in the same repository or do we attempt to encourage one repository per deployable service?

Here, we considered Conway's law, and the application of Conway's Law: the Inverse Conway Manoeuvre. Can we design or recommend a solution that "encourages" the right collaboration between teams, and discourages coupling between teams that shouldn't work together as much? Similarly, we want to talk about Coupling vs Cohesion. Coupling we are familiar with, as it is a constant problem in our existing legacy system, but overcorrecting can lead to issues with "cohesion." We want to loosely couple dissimilar or independent concepts/teams, but on the other hand we want similar things grouped together. Completely decoupling every single concept leads to a "distributed monolith" which is often worse than a regular monolith.

So what we landed on was providing a template that would support a Monorepo approach, where teams would each have their own Monorepos. The trade-off is that services owned by a single team will likely be more coupled, but as a result, the team can more easily act across the related functionalities that they own. Such as a single PR across multiple closely related services. This still allows for the natural decoupling provided by other teams having their own monorepos. This of course relies on us making sure team boundaries line up well with domain boundaries.

### Packages or Modules
Within a monorepo, we now need to consider boundaries between the individual services. We have colocated them in a monorepo to allow for more cohesion, but we still don't want this team to spin up into its own monolith.

Modules provide a way to build separately deployable artifacts (multiple services) and provide harder boundaries between the shared aspects of these services. Packages are extremely easy to create / move / update. Modules are more difficult and therefore rely on less developer discipline to respect. For instance, it's easy to import an ORM in a package based application into the business logic, thus coupling them together, but it's more painful to add a dependency to the service's POM. It's not as hard as integrating with an external service, but it makes it harder to introduce coupling. It also forces developers to consider the dependency graph, since this would introduce a circular dependency in the module's dependencies. This is often less visible with packages. Again, the tradeoff here is stronger boundaries (and therefore less reliance on developer discipline) in exchange for technical complexity (needing to learn about multi module projects and have a better understanding of maven).

### Boundaries
To summarize so far: we are concerned about enforcing boundaries appropriately at all levels of a team's code, so that adding coupling (and preventing the platform from scaling, our current top priority) becomes harder, and doing the right thing (respecting the boundaries) becomes the easier path forward for a feature. So we decouple teams and domains via microservices and independently deployable applications. We allow for easier collaboration between services owned by a single team by colocating the code in a monorepo. But we keep the services independently deployable and loosely coupled via a multimodule project. We also take advantage of the multimodule approach to enforce a "ports and adapters" style architecture, which enables maximum velocity for applications where business logic is the primary constraint (which is most of the ecomm domains).

## Services, Adapters, Libraries, and Apps
In order to encourage and enforce a ports and adapters style architecture, we have broken down services into several components: services, adapters, Libraries, and Apps. The term service is a bit overloaded since a microservice refers to an entire application, but this seems to be the language most people are comfortable with, everyone will just need to be aware of the context when discussing a "service."

### Service
A service module is where we house all the business logic and create "ports" that are required to drive behavior (and remember, the service itself is a "port" that is consumed by the transport layer for inbound requests).

For example, the UserService looks like this:
```kotlin
interface UserServicePort {
    fun createUser(request: CreateUserCommand): User
    fun getAllUsers(): List<User>
    fun getUser(userId: String): User
    fun notifyTaskCreated(taskCreatedEvent: TaskCreatedEvent): List<User>
}

@Service
class UserService(
    private val userRepo: UserRepositoryPort,
    private val meterRegistry: MeterRegistry,
) : UserServicePort {

    private val usersCounter = meterRegistry.counter("todo_users_counter")

    override fun createUser(request: CreateUserCommand): User {
        val user = userRepo.upsertUser(
            User(
                firstName = request.firstName,
                lastName = request.lastName,
                contactInfo = ContactInfo(
                    email = request.email,
                )
            )
        )
        usersCounter.increment()
        return user
    }

    override fun getAllUsers(): List<User> {
        return userRepo.findAll()
    }

    override fun getUser(userId: String): User {
        return userRepo.findByUserId(UUID.fromString(userId))
            ?: throw IllegalArgumentException("UserId not found")
    }

    override fun notifyTaskCreated(taskCreatedEvent: TaskCreatedEvent): List<User> {
        return userRepo
            .findMany(taskCreatedEvent.watchers)
            .sendTaskCreatedMessages(taskCreatedEvent.taskId)
            .map(userRepo::upsertUser)
    }
}
```

The service is where we compose our domain model, which houses pure business rules, with the infrastructure required to do something meaningful with those rules.

The service module defines the ports (as interfaces) that it requires to function, and relies on the calling code to provide those adapters.

For example, the `UserService` requires the ability to persist and fetch `User` aggregates, so we define a port
```kotlin
interface UserRepositoryPort {

    fun findByUserId(userId: UserId): User?

    fun findAll(): List<User>

    fun upsertUser(user: User): User
}
```

We can then program against this interface without ever needing to know how it is implemented. We can use in-memory versions for tests, and jOOQ, JPA, Postgres, Mongo, etc in production. But the service never needs to change to support that.

One of the major benefits of this architecture is that we have separated the domain logic from the infrastructure, which means we can easily test the business logic in isolation with unit tests AND we can swap out infrastructure without any effect on the business logic. Bad experience with JPA? Implement a jOOQ adapter and plug it in! Relational databases turned out to be a bad fit? Plug in a document storage solution! Want to switch to GraphQL from rest? No problem, just call the service methods. The service never needs to change for infrastructure reasons.

This style of architecture is extremely testable, maintainable, extensible, and refactorable.

And by loosely coupling the domain from the infrastructure, we can make it as easy as possible to refactor business logic, given the safety of a suite of use case driven unit tests with no technical coupling.

### Adapters
Adapters are implementation of ports, or in other words: concrete implementations.

The service provided the `UserRepositoryPort` and in `out-jpa` we implement that port using JPA and Postgres:

```kotlin
@Component
class UserRepository(
    @Autowired val userEntityRepository: UserEntityRepository
) : UserRepositoryPort {
    override fun findByUserId(userId: UserId): User? =
        userEntityRepository
            .findByIdOrNull(userId)
            ?.toDomain()

    override fun findAll(): List<User> =
        userEntityRepository.findAll().map { it.toDomain() }

    override fun upsertUser(user: User): User =
        user.also { userEntityRepository.save(user.toEntity()) }
}
```

We similarly have an implementation with jOOQ instead of JPA. The adapters are purely focused on technical implementation, carry little to know business logic, and are "plug and play" with the service.

Similarly, we provide an adaptor for getting requests to the service with `in-transport`.

```kotlin
@RestController
@RequestMapping(Routes.USERS)
class UserController(
    private val userService: UserServicePort,
    private val meterRegistry: MeterRegistry,
) {
    @GetMapping
    @Timed(value = "todo_get_users")
    fun getAllUsers() =
        userService.getAllUsers().map { it.toRest() }

    @GetMapping("/secure")
    fun getUsersSecure() =
        userService.getAllUsers().map { it.toRest() }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String) =
        userService.getUser(userId).toRest()

    @PostMapping
    @Timed(value = "todo_create_user")
    fun createUser(@RequestBody request: CreateUserCommand) =
        userService.createUser(
            com.onepeloton.todo.service.CreateUserCommand(
                request.firstName,
                request.lastName,
                request.email
            )
        ).toRest()
}
```
We also provide a graphql api and a kafka listener; the service doesn't know or care how the requests are coming in. You could make a CLI, it's completely isolated from the business logic, concerns are separated.

### Libraries
These are modules for generic non-business behavior that is too general to be an adapter. Something like a common testing library, a common date library/standard, shared behavior abstracting over Kafka, etc. These are extremely general and are used by the service and adapters as appropriate.

Libraries in the project are for shared functionality within the service block. Once multiple service blocks use it, it should be graduated to a shared library with artifactory.

### Applications
With the above types of modules, we have all the pieces we need to assemble an application. Like legos, all you need to do is pick the ones you want.

The service requires a UserRepository and a MeterRegistry, so we can plug those into the app (and they are automatically wired up with Spring).

```xml
        <!-- Adapters -->
        <!-- Graphql, Rest, and Kafka controllers -->
        <dependency>
            <groupId>com.onepeloton.todo</groupId>
            <artifactId>todo-adapter-in-transport</artifactId>
            <version>${project.version}</version>
        </dependency>

        <!-- Datadog's MeterRegistry implementation -->
        <dependency>
            <groupId>com.datadoghq</groupId>
            <artifactId>dd-java-agent</artifactId>
            <version>${datadog.javaagent.version}</version>
            <scope>runtime</scope>
        </dependency>

        <!-- jOOQ implementatino of UserRepository -->
        <dependency>
            <groupId>com.onepeloton.todo</groupId>
            <artifactId>todo-adapter-out-jooq</artifactId>
            <version>${project.version}</version>
        </dependency>
```

We use Spring to automatically wire everything up. So simply by adding these dependencies we have created a deployable working app.

We are simply composing our choice of adapters with the service and deploying them together as an application!

## Dependency Graph
This template is provided and structured with the idea that the domain model should be at the "center" of your dependency graph. That is instead of having an n-layered arch that many engineers are used to, imagine a circle (or hexagon) with the domain in the center, and everything else around the circle pointing inward towards the domain.

In our structure, that means that adapters depend on the service module, not the other way around. Similarly, the app depends on the adapters and the service.

Libraries are special generic bits of functionality, so they can be used anywhere.
