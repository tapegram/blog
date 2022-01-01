# PORTS AND ADAPTERS

## Problem Context
Ports and Adapters is a top level pattern that describes a high level strategy for separating and composing code. In particular, it describes how to separate *infrastructure* from *logical* code.

Ports and Adapters is pretty much always a sure bet to improve the architecture of a system, so instead of defining what problem context you *should* use it in, I will give some examples of when it may be inappropriate.
1) You are using a programming language that enforces function purity (this [strongly correlates to ports and adapters, so you don't need to think about it](https://www.youtube.com/watch?v=cxs7oLGrxQ4&ab_channel=NDCConferences))
2) You are working on throw away code that will never need to be maintained, extended, tested, or updated.
3) There is no business logic, only infrastructure. Conversely, there is no infrastructure, only business logic. These should only be possible when publishing libraries for others to use, though those libraries will most easily be put to use in a ports and adapters style arch.

## Application
The main idea behind Ports and Adapters is "dependency inversion" via interfaces, or whatever interface-like tools are available in your chosen language.

Traditional n-tiered architectures attempt to separate infrastructure into layers around the "domain." This often results in a domain layer that is tightly coupled to infrastructure. Such as a domain layer is then dependent on some particular db technology or ORM. It may even be dependent on a framework or presentation layer. This is the worst position you can be in if you are working in a problem context defined by the complexity of its domain! All of this coupling will make testing significantly harder and slower (possibly needing to test business logic with a real or expensively mocked db, for example). For the same reasons, it will become very difficult to refactor the domain, so you won't be able to refine and iterate on it as easily, which is where the value to the organization is generated.

Ports and Adapters reimagines this "layered arch" into a sphere or "hexagon" where the "core" is the domain, with a thin wrapper of "application logic," and then all infrastructure plugging into the application logic from the outside. As a result, the direction of dependencies goes inward, with the domain having little to no dependencies.

Let me give two examples. Here is an example of a tightly coupled 3-tiered arch.

```kotlin
class ItemRequest(
    val sku: String,
)

class CartEntity(
  val id: String,
  val items: List<Item>
)

class ItemEntity(
  val id: String,
  val sku: String,
  val price: Int,
)

@RestController
@RequestMapping("/api")
class CartController {

  @PostMapping("/cart/{id}/item")
  fun addItemToCart(
    @PathVariable id: String,
    @RequestBody request: ItemRequest,
  ): CartEntity {
    val cart = SomeOrmFramework.findCartById(id)
    val product = SomeOrmFramework.findProductBySku(sku)
    val item = SomeOrmFramework.createItem(product.sku, product.price)

    cart.addItem(item)

    SomeOrmFramework.update(cart)
    return cart
  }
}
```

It was painful to write that example. There are a ton of issues here that are going to make maintaining this otherwise simple code increasingly difficult.

What if we want to expose another endpoint that reuses this functionality? What if we want to expose the same behavior via a graphql mutation or grpc? We can either copy + paste this code (the worst possible idea), or we could try to refactor this behavior into some function like this

```kotlin
fun addItemWithSku(cartId: String, sku: String) {
  val cart = SomeOrmFramework.findCartById(id)
  val product = SomeOrmFramework.findProductBySku(sku)
  val item = SomeOrmFramework.createItem(product.sku, product.price)

  cart.addItem(item)

  SomeOrmFramework.update(cart)
  return cart
}
```

Now it's reusable! But how do we test this? In order to test this we need to either spin up a real or sort of real db (like h2) and prepopulate it with values. This is slow, verbose, and painful. This will result in people avoiding writing thorough tests.

Additionally, what if we want to change the ORM or DB technology? It would not be easy! If this application was sufficiently large, I'd probably just refuse to even entertain that idea.

Even worse, we are using the data model when describing the logic and the presentation layer! We could refactor to add some more mapping with intermediate steps to allow for such benefits as: representing the cart as a sealed class, so you can introduce a state-machine and some cool type safety. However, in my experience, this falls apart over time as new or time pressed engineers see this code and just keep piling on to the data model since its easy.

This is also likely nonperformant, since doing everything with the DB could result in unnecessary round trips, instead of performing logic purely in memory after fetching all the required data in one go.

This architecture also encourages designing from the data model up instead of focusing on the domain model, which is a missed opportunity.

As a result, you end up with code that is hard to reuse, is platform/technology dependent, is hard or impossible to test, and is non performant. Worse than that, [this architecture encourages all of these properties, even when you and your team try to actively fight against it.](https://www.youtube.com/watch?v=US8QG9I1XW0&ab_channel=NDCConferences)


Let's retry this with a ports and adapters approach.

First let's define the core of the application.

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

Cool here are some quick and dirty domain models. Plain old kotlin objects (with some immutability because why not). I'm anticipating on the Cart being the aggregate in a shopping cart based system, for obvious reasons.

Now let's do some ports and adapters magic!

```kotlin
interface CartRepo {
  fun findById(cartId: UUID): Cart
  fun save(cart: Cart): Unit
}

interface ProductCatalogRepo {
  fun findBySku(sku: Sku): Product
}

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

It looks pretty similar to before, but with some key differences!

1) CartRepo and ProductCatalogRepo are interfaces instead of concrete implementations. We require that whoever instantiates the CartService provide implementations of these interfaces, but as long as they uphold their end of the interface, we don't care what technology is used to implement them!
2) We use separate models to represent the domain which means we can use the features available in the language that aren't otherwise serializable to fully represent the logic of the Cart. If we wanted to do something like this 
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
  ): Cart()
}
```
There's some other stuff to update, but we have free rein of refining the model to take advantage of the type system without needing to worry about having to represent this directly in the RDS or a document.

But how do we use this? Easy! We build "adapters" for the above "ports."

For the DB access

```kotlin
object ORMCartRepo : CartRepo {
  override fun findById(cartId: UUID): Cart {
    return SomeOrmFramework.findCartById(id).toCart()
  }

  override fun save(cart: Cart): Unit {
    return SomeOrmFramework.update(cart.toEntity())
  }
}

object ORMProductCatalogRepo: ProductCatalogRepo {
  fun findBySku(sku: Sku): Product {
    return SomeOrmFramework.findProductBySku(sku).toProduct()
  }
}
```

For the rest controller

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

Wow, that controller got a lot thinner!

And now you can easily test the domain independent of infrastructure by creating a couple simple in memory repos.

```kotlin
data class InMemoryCartRepo(private val carts: MutableList<Cart>) : CartRepo {
  override fun findById(cartId: UUID): Cart =
    carts.find { it.id == cartId }

  override fun save(cart: Cart): Unit {
    carts.remove { it.id == cart.id }
    carts.add(cart)
  }
}

data class InMemoryProductCatalogRepo(private val products: List<Product>): ProductCatalogRepo {
  fun findBySku(sku: Sku): Product =
    products.find { sku == it.sku }
}
```

And here's what some tests might look like

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
        result.items.length shouldBe 1
        result.items[0].sku shouldBe product.sku
        result.items[0].price shouldBe product.price
      }
    }

    "throws an exception if the sku doesn't exist" {
      shouldThrow<SkuDoesNotExist> {
        service.addItem(cart.id, "alkjsfalksjbg")
      }
    }

    "throws an exception if the cart doesn't exist" {
      shouldThrow<SkuDoesNotExist> {
        service.addItem(UUID.randomUUID(), product.sku)
      }
    }
})
```

The adapters can be tested separately as part of integration tests.


## Resulting Context
Ports and Adapters decouples business logic from infrastructure. This has the benefits of

1) Testing the business logic is easier, cheaper, and faster.
2) Technology choices become "implementation details" that can be swapped out with no effect on the domain. Want to switch from Postgres to DynamoDB? That project just went from "MEGA" to "Small."

However, one of the biggest problems with Ports and Adapters is that it is maintained purely through developer discipline. Anyone can submit a PR that breaks this architecture, and you immediately start losing the benefits. This can lead to a lot of energy spent convincing well-meaning junior and even senior engineers not to merge commits that they consider perfectly valid because they don't know about or see the benefits of the chosen architecture.

In order to reduce the personal and mental maintenance cost of a ports and adapters architecture, one may try to create a ["pit of success."](https://www.youtube.com/watch?v=US8QG9I1XW0&ab_channel=NDCConferences) by [ENFORCING REFERENTIAL TRANSPARENCY](enforce_referential_transparency.md)

Additionally, you will likely still run into some problems with tests directly depending on domain objects, which can make the tests fragile and the domain harder to refactor. If refactoring the domain is your highest priority (as it should be in a context demanding Domain Driven Design), you may want to consider applying [HIDE THE DOMAIN.](hide_the_domain.md)