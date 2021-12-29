# Birthday Kata

This is a simple application demonstrating the application of the testing patterns for the related blog post.

The exercise is my favorite kata: [The Birthday Greeting Kata](http://matteo.vaccari.name/blog/archives/154.html).

Feel free to browse through the commits but you will probably see
1) Scaffolding of the domain and service
2) TDD + Refactoring

With patterns such as 
- [TEST DRIVEN DEVELOPMENT](../../patterns/test_driven_development.md)
- [PORTS AND ADAPTERS](../../patterns/ports_and_adapters.md)
- [ENFORCE REFERENTIAL TRANSPARENCY](../../patterns/enforce_referential_transparency.md)
- [HIDE THE DOMAIN](../../patterns/hide_the_domain.md)
- [KEEP THE DOMAIN PURE](../../patterns/keep_the_domain_pure.md)
- [MODEL DOMAIN ERRORS](../../patterns/model_domain_errors.md)
- [TEST USECASES](../../patterns/test_usecases.md)
- [MAKE A DSL FOR USECASES](../../patterns/make_a_dsl_for_usecases.md)

I've chosen to make a simple application, though this style of code scales well as a collection of projects. I've also chosen to add arbitrary "business logic" to this kata to complicate the domain event.

Hopefully, you can browse through the tests to get a clear idea of what the usecases are!