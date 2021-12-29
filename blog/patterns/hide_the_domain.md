# HIDE THE DOMAIN

## Problem Context
If you are applying [PORTS_AND_ADAPTERS](ports_and_adapters.md) you have inverted your application's dependencies so that everything points towards the domain.

This is good, but it does introduce a small set of problems. Now that everything depends on your domain model, any change to the domain model can require cascading changes to all of the rest of your application! Similar to how changes to the data model had a cascading effect on the rest of the layers in a poorly isolated n-tier architecture.

This is no good since one of the major benefits of PORTS AND ADAPTERS is making it easier to refactor and refine these models by decoupling them from infrastructure. Now the problem has changed from the domain being tightly coupled to the infrastructure, to the infrastructure being coupled to the domain! Again, this is a better problem to have, but it will also slow your team down and cause issues in the long run.

If you are applying [TEST DRIVEN DEVELOPMENT](test_driven_development.md), this problem will also undercut your ability to refactor easily. Tests will "calcify" your domain if they test the domain models directly.

For example, let's say we have something like this:

An overly simple domain model
```kotlin
data class Cart(
  val id: UUID,
  val items: List<Item>,
) {
  fun addItem(item): Cart = TODO("Do whatever validation needs to be done and add the item to the cart")
}

data class Item(
  val id: UUID,
  val sku: Sku,
  val price: Price,
)
```

A service
```kotlin
data class CartService(
  val cartRepo: CartRepo,
  val productCatalogRepo: ProductCatalogRepo,
) {
  fun addItem(cartId: UUID, productSku: String): Cart {
      val cart = cartRepo.findById(cartId)
      val product = productCatalogRepo.findBySku(productSku)

      return cart.addItem(product.toItem())
        .also { cartRepo.save(cart) }
  }
}
```

An adapter
```kotlin
class ItemRequest(
    val sku: String,
)

@RestController
@RequestMapping("/api")
class CartController(
  val cartService: CartService
) {

  @PostMapping("/cart/{id}/item")
  fun addItemToCart(
    @PathVariable id: String,
    @RequestBody request: ItemRequest,
  ): Cart =
    cartService.addItem(id, request.sku)
}
```

And tests
```kotlin
val cart = Cart(UUID.randomUUID(), items = emptyList())
val cartRepo = InMemoryCartRepo(
  mutableListOf(cart)
)

val product = Product(UUID.randomUUID(), sku = "shirt-blue", price = 100)
val productRepo = InMemoryProductCatalogRepo(
  listOf(product)
)

val service = CartService(cartRepo, productRepo)

class CartTest : StringSpec({
    "successfully adds an item to the cart" {
      val result = service.addItem(cart.id, product.sku)
      assertSoftly {
        result.items.length `shouldBe` 1
        result.items[0].sku `shouldBe` product.sku
        result.items[0].price `shouldBe` product.price
      }
    }

    /*
    Imagine dozens of other happy path tests that look similar to the above test.
    Testing all kinds of different products, prices, etc.
    */

    "throws an exception if the sku doesnt exist" {
      shouldThrow<SkuDoesNotExist> {
        service.addItem(cart.id, "alkjsfalksjbg")
      }
    }

    "throws an exception if the cart doesnt exist" {
      shouldThrow<SkuDoesNotExist> {
        service.addItem(UUID.randomUUID(), product.sku)
      }
    }
})
```

Let's say we want to do something good and MAKE THE AGGREGATE A STATE MACHINE so we can better represent the domain and introduce some type-safety. In particular, we want to disallow updating carts that are "closed," or that have already successfully checkout out.

```kotlin
sealed class Cart {
  data class Open(
    val id: UUID,
    val items: List<Item>,
  ): Cart() {
    fun addItem(item): Cart = TODO("Do whatever validation needs to be done and add the item to the cart")
  }

  data class Closed(
    val id: UUID,
    val items: List<Item>,
    val orderId: UUID,
  ): Cart()
}
```

Now we have an open cart that one can add items to and a closed cart that has a pointer to the order that was created from it after checkout but is otherwise impossible to update.

Great! Of course, we will need to update the service a bit for this to work correctly:

```kotlin
data class CartService(
  val cartRepo: CartRepo,
  val productCatalogRepo: ProductCatalogRepo,
) {
  fun addItem(cartId: UUID, productSku: String): Cart {
      val cart = cartRepo.findById(cartId)
      
      /* This could be better but is ok for now */
      when (cart) {
        is Open -> Unit // Good lets keep going
        is Closed -> throw CartIsClosed
      }

      val product = productCatalogRepo.findBySku(productSku)

      return cart.addItem(product.toItem())
        .also { cartRepo.save(cart) }
  }
}
```

But now this is incompatible with our REST adapter! We have to tell it how to handle the new domain types (it doesn't know how to serialize `Cart` and it doesn't know what `Cart.Open` is)

We could change `addItem`'s return type to `Cart.Open` for now. But we still have to update the adapter to handle this new type. If we just added new fields or removed ones from the domain object, we would be forced to update every adapter that knew about that field. 

Similarly, any test that knows about the shape or implementation of the domain model will need to be updated.

This can get really annoying and can end up discouraging refactoring refining the domain model. It can also discourage taking advantage of your language's type system to express the domain since those language features are often not serializable.

This problem can be made worse if our service's type signature included domain types in its arguments as well as the response. Such as

```kotlin
data class CartService(
  val cartRepo: CartRepo,
  val productCatalogRepo: ProductCatalogRepo,
) {
  fun addItem(cartId: UUID, productSku: String): Cart {
      val cart = cartRepo.findById(cartId)
      val product = productCatalogRepo.findBySku(productSku)

      return cart.addItem(product.toItem())
        .also { cartRepo.save(cart) }
  }

  /* 
  Every time Item changed shape, every adapter (including tests) would need to update the args
  passed in.
  */
  fun addItem(cartId: UUID, item: Item): Cart = TODO()
}
```

## Application
Don't let the Service leak domain types! That is, the service should have its own request and response types it maps internally to domain types. That way the adapters calling the service, and tests, don't need to be updated when the domain model is refactored! Just the service itself.


```kotlin
sealed class Commands {
  data class AddItem(val cartId: UUID, productSku: String): Commands()
}

data class CartResponse(
  val id: UUID,
  val items: List<ItemResponse>
)

data class ItemResponse(
  val id: UUID,
  val sku: String,
  val price: Int,
)

data class CartService(
  val cartRepo: CartRepo,
  val productCatalogRepo: ProductCatalogRepo,
) {
  fun addItem(command: Commands.AddItem): Cart {
      val cart = cartRepo.findById(cartId)
      val product = productCatalogRepo.findBySku(productSku)

      return cart.addItem(product.toItem())
        .also { cartRepo.save(cart) }
        .toResponse()
  }
```

With this extra level of indirection, now the adapters depend on the Service level command and response models, and the domain models are fully encapsulated.

## Resulting Context
As a result, your domain model is fully encapsulated by the service layer. The service layer, representing "application logic" is much more stable than the domain layer, so as a result, these types provide a more stable foundation for your in-bound adapters to depend on, including tests. This means that you could theoretically completely rewrite your domain model and be able to reuse your entire existing test suite. That is incredibly powerful -- and once you have the ability to do that, I guarantee you will at least once.

However, this approach is not without tradeoffs.

The `response` model sometimes can feel wasteful since it will closely resemble the domain model, though it will likely diverge over time in order to better map to serializable boundaries. That is, your response models will have a `state` field instead of being represented by a sealed class, they will likely make ore use of primitive values, etc.

If you find that this approach feels increasingly wasteful, it may be because you are implementing a read heavy service with domain models, which are optimized for writes. If you are light on business logic, this pattern (and many others here) may not be appropriate for you.

Additionally, out-bound adapters will still need to depend directly on domain objects. In my experience, these are often limited to repositories or some occasional adapters for communicating with third-party services and are easy to update. The biggest win for this pattern is ensuring that your domain is refactorable by loosely coupling with tests.