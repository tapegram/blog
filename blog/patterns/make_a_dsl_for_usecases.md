# MAKE A DSL FOR USECASES

## Problem Context
If you are [TESTING USECASES](test_usecases.md) you may eventually run into issues with the sheer verbosity of test setup required to make up for a lack of unit testing in your system.

Similarly, this setup tends to become difficult to read over time without proper investment and documentation.

## Application
As you write your tests, attempt to build out a DSL for doing your test setup that uses the ubiquitous language of your domain.

I personally prefer a builder syntax, though Kotlin has great support for building DSLs with lambdas.

Lets say this is what our tests look like now
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

    "items with limited purchase amounts cant be exceeded" {
      /*
      Do a ton of setup to create a limited product
      */
      val result = service.addItem(cart.id, limitedProduct.sku)

      assertSoftly {
        result.items.length `shouldBe` 1
        result.items[0].sku `shouldBe` product.sku
        result.items[0].price `shouldBe` product.price
      }

      shouldThrow<ProductLimitExceeded> {
        service.addItem(cart.id, limitedProduct.sku)
      }
    }

    "items with limited purchase amounts can be added if less than the max amount" {
      /*
      Do a ton of setup to create a limited product
      */
      service.addItem(cart.id, limitedProduct.sku)
      val result = service.addItem(cart.id, limitedProduct.sku)

      assertSoftly {
        result.items.length `shouldBe` 2
      }
    }

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

Let's build up a DSL to use in our tests
```kotlin
data class Given(
  val cartRepo: CartRepo = InMemoryCartRepo(mutableListOf()),
  val productCatalogRepo: ProductCatalogRepo = InMemoryProductCatalogRepo(mutableListOf()),
) {
  fun `a product`(id: UUID = UUID.randomUUID(), sku: String = "product", price: Int = 100, purchaseLimit: Int? = null) {
    productCatalogRepo.add(Product(id, sku, price, purcahseLimit))
    return this
  }

  fun `a blue shirt`(id: UUID = UUID.randomUUID(), price: Int = 100) {
    `a product`(id = id, sku="blue-shirt", price = price)
    return this
  }

  fun `a limited availability snowglobe`(id: UUID = UUID.randomUUID(), price: Int = 999, purchaseLimit: Int? = 1) {
    `a product`(id = id, sku="snowglobe", price = price, purchaseLimit: Int? = 1)
    return this
  }

  fun `a cart with`(id: UUID = UUID.randomUUID, skus: List<Sku> = emptyList()) {
    val cart = Cart(id, items = listOf(skus.map { productCatalogRepo.findBySku(it) })) 
    cartRepo.save(cart)
    return this
  }

  fun when() = CartService(cartRepo, productCatalogRepo)
}
```

Please don't judge this too harshly, there are a lot of ways this can be reimagined and improved, but I hope the idea of it seems good. If I was spending more time on this, I might want to fully flesh out Given, When, and Then DSL to even abstract over assertions. Check out the Kotlin docs on [type-safe builders](https://kotlinlang.org/docs/type-safe-builders.html#how-it-works).

With this DSL we can update our tests like so

```kotlin
val cartId = UUID.randomUUID()

class CartTest : StringSpec({
    "successfully adds an item to the cart" {
      val result = Given()
        .`a blue shirt`()
        .`a cart with`(cartId, emptyList())
        .when()
        .addItem(cartId, "blue-shirt")

      assertSoftly {
        result.items.length `shouldBe` 1
        result.items[0].sku `shouldBe` "blue-shirt"
        result.items[0].price `shouldBe` 100
      }
    }

    "items with limited purchase amounts cant be exceeded" {
      shouldThrow<ProductLimitExceeded> {
        Given()
          .`a limited availability snowglobe`()
          .`a cart with`(cartId, listOf("snowglobe))
          .when()
          .addItem(cartId, "snowglobe")
      }
    }

    "items with limited purchase amounts can be added if less than the max amount" {
      val result = Given()
        .`a limited availability snowglobe`()
        .`a cart with`(cartId, emptyList())
        .when()
        .addItem(cartId, "snowglobe")

      assertSoftly {
        result.items.length `shouldBe` 1
        result.items[0].sku `shouldBe` "snowglobe"
        result.items[0].price `shouldBe` 999
      }
    }
})
```

## Resulting Context
The result of applying this pattern is a deeper understanding of the problem domain and better tooling for writing "good" tests (refactorable and ensures correctness). This DSL will allow you to crank out realistic test cases with the proper amount of abstraction.

Even better, junior engineers can pick this up with little or no coaching and you can better collaborate with product and the business since you directly translate requirements into test cases!

By extending and maintaining this DSL youare forcing yourself to better understand the domain, and at the same time producing excellent, type-safe documentation.

I have been writing tests like this for a while now and I can not recommend it enough. It saves an enormous amount of time when a new engineer can pick up a ticket and just look at the tests to understand the problem domain, and then in-turn can easily write tests and get to work, without me even getting involved!