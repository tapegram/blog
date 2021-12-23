# MODEL DOMAIN ERRORS

## Problem Context
When modeling a system, a great deal of the complexity comes from what to do when things "fail." For instance, what happens on subscription billing if a credit card is declined? What if there is some transient technical error? These are all known "errors" that may well be part of the business logic for an application. If a card is declined, maybe we want to prompt the user for another card. If this is for a charge happening for something recurring like a subscription, maybe we need to update the state of the subscription and email the customer to update their payment method. Or maybe the charge failed for another reason that is more transient -- maybe we can just wait a day and try again? This is all part of the "business" logic, but often is neglected in the model (and tests) due to a bias towards the "happy path."

```kotlin
interface PaymentGateway {
  suspend fun capture(paymentMethodId: UUID, amount: Int): Unit
}

interface SubscriptionRepo {
  suspend fun get(subscriptionId: UUID): Subscription
  suspend fun save(subscription: Subscription): Unit
}

data class SubscriptionBillingService(
  val paymentGateway: PaymentGateway,
  val subscriptionRepo: SubscriptionRepo,
) {
  suspend fun charge(subscriptionId: UUID): SubscriptionResponse {
    val subscription = subscriptionRepo.get(subscriptionId)
    val result = paymentGateway.capture(subscription.paymentMethodId, subscription.amountDue)
    subscriptionRepo.save(subscription.addPayment())
  }
}
```

This code only handles the happy path! And you can tell from the two interfaces that there could be failures that that we are ignoring.

Capture could fail for a variety of reasons such as the card being declined or the payment method not being found. Similarly, the repo could fail to find the subscription or saving could fail because of an outage. We could throw a bunch of try catches everywhere instead

```kotlin
data class SubscriptionBillingService(
  val paymentGateway: PaymentGateway,
  val subscriptionRepo: SubscriptionRepo,
) {
  suspend fun charge(subscriptionId: UUID): SubscriptionResponse {
    try {
      val subscription = subscriptionRepo.get(subscriptionId)
    } catch (e: RuntimeException) {
      /*
      Wait, I don't actually know what exceptions get thrown by this function???? Guess I have to go to its definition and dig around.
      Oh it calls other functions that throw exceptions? So I'll never really know if I'm handling the failure cases??
      */
      throw ChargeFailed("Failed when fetching subscription: ${e.message}"))
    }
    try {
      val result = paymentGateway.capture(subscription.paymentMethodId, subscription.amountDue)
    } catch (e: RuntimeException) {
      throw ChargeFailed("Failed during capture: ${e.message}"))
    }

    try {
      subscriptionRepo.save(subscription.addPayment())
    } catch (e: RuntimeException) {
      throw ChargeFailed("Failed during save: ${e.message}"))
    }

    return subscription.toResponse()
  }
}
```

There are of course ways to try to make this better (since this is the way most people write code), so I don't want to necessarily make a "strawman" here. But this obviously needs so work. All of the failure handling is low level try/catch blocks that clutter the code and make it hard to understand what is going on.

Also, as mentioned in the comments, it's hard to tell what exceptions can be raised by different parts of the code. You either have to trust the documentation, catch weird generic errors and parse out their string messages to tell what happened, or dig deep into the implementation details. This leads to bugs, confusion, and even when it works its slow and difficult to maintain.

As a side note, if you are wondering why we don't just used checked exceptions, they aren't supported in Kotlin by design. Check out the documentation [here](https://kotlinlang.org/docs/exceptions.html#the-nothing-type). The kotlin docs helpfully link out to other articles like [Java's Checked Exceptions Were a Mistake](https://radio-weblogs.com/0122027/stories/2003/04/01/JavasCheckedExceptionsWereAMistake.html).

## Application
To make these paths first class you should model domain errors explicitly in your domain model. 

```kotlin
object Error

sealed class CaptureFailure: Error {
  data class CardDeclined(val message: String): SubscriptionChargeFailure()
  data class PartnerUnavailable(val message: String): SubscriptionChargeFailure()
}
interface PaymentGateway {
  suspend fun capture(paymentMethodId: UUID, amount: Int): Either<CaptureFailure, Unit>
}

sealed class GetFailure: Error {
  data class NotFound(id: UUID): GetFailure()
  data class CouldNotCompleteRequest(message: String): GetFailure()
}

sealed class SaveFailure: Error {
  data class CouldNotCompleteRequest(message: String): SaveFailure()
}

interface SubscriptionRepo {
  suspend fun get(subscriptionId: UUID): Either<GetFailure, Subscription>
  suspend fun save(subscription: Subscription): Either<SaveFailure, Unit>
}
```

Now you can do something like
```kotlin
data class SubscriptionService(
  val paymentGateway: PaymentGateway,
  val subscriptionRepo: SubscriptionRepo,
) {
  suspend fun charge(subscriptionId: UUID): Either<Error, SubscriptionResponse> = either {
    val subscription = subscription.get(subscriptionID).bind()
    val captureResult = paymentGateway.capture(subcription.paymentMethodId, subscription.amountDue)

    val updatedSubscription = captureResult.fold(
      ifLeft: { when (it) {
          is CardDeclined -> subscription.setDelinquent(reason = Reason.CardDeclined)
          is PartnerUnavailble -> subscription.setDelinquent(reason = Reason.Unknown(message = it.message))
        }
      },
      ifRight: { subscription.addSuccessfulPayment() }
    )

    subscriptionRepo.save(updatedSubscription).bind()
    updatedSubscription.toResponse()
  }
}
```

This might take a little getting used to because we are using the [Arrow library to provide tooling that makes using `Either` a lot nicer.](https://arrow-kt.io/docs/apidocs/arrow-core/arrow.core/-either/).

But you can see that exceptions are only appearing in this code where it drives business logic: when it affects how we updated a subscription when capture fails. Failures like get/save are handled implicitly by the `either` block, they short-circuit by return the error. 

This is much more declarative and less cluttered than the previous code.

Additionally, we are coding to contract using the type system. The compiled let's us know which errors we need to handle when we use a `when`. Similarly, using `Either` makes it hard to ignore failure cases since you have to provide a handle for both the left and right sides of an Either in `fold` in order to get access to the "happy path side.

## Resulting Context
The explicit modeling of domain errors in your code significantly improved the maintainability and expressiveness of your code.

By modeling the errors explicitly, you can code to contract instead of needing to break the abstraction to dig into what exceptions may or may not be thrown (which is a general problem when dealing with side effects). You are better equiped to express the problem space in your domain model since, let's face it, most of the complexity of a domain comes from how to handle failures, not the happy path cases.

Honestly, by writing code in this style, the compiler constantly reminds me that I am trying to be lazy and avoid handling error cases all the time (it reminds me by failing to build). Modeling errors like this quickly becomes a critical tool for engineers working in complicated domains and leads to a better understanding of the domain itself.

For a much better walkthrough of this style of coding (as well as another perspective on the tradeoffs) check out [Railway Oriented Programming](https://fsharpforfunandprofit.com/rop/) as well as the Arrow team's docs on [Either](https://arrow-kt.io/docs/apidocs/arrow-core/arrow.core/-either/)

Note that these code examples also make use of [PORTS AND ADAPTERS](ports_and_adapters.md), [HIDE THE DOMAIN](hide_the_domain), [KEEP THE DOMAIN PURE](keep_the_domain_pure.md), and [ENFORCE REFERENTIAL TRANSPARENCY](enforce_referential_transparency.md).

Also note that `Either` is a gateway to FP. `Either` is an incredible tool but it has it's own downsides that aren't really addressed anywhere except in FP toolkits (for instance, the use of the `either` computation arrow provides to give us this nice imperative syntax without deep nesting). I consider this a plus -- it's easy to convince engineers of the value of `Either`, and then they will naturally learn FP as they use it without necessarily being overwhelmed like one would if they tried to tackle FP all at once and with less concrete usecases. However, if you a rabidly against FP, you may need to let the problems this patterns addresses go unsolved.