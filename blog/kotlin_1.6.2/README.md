# Context Receivers!

## Background
This post is a write-up of my experiences playing around with the new context receivers prototype in Kotlin 1.6.2.

See [the KEEP](https://github.com/Kotlin/KEEP/blob/master/proposals/context-receivers.md) for more thorough documentation.

Additionally, you may wish to look at Simon Vergauwen's [great blog post exploring the same feature](https://nomisrev.github.io/context-receivers/) for another angle.

I've been very interested in language features that help provide a ["pits of success"](https://www.youtube.com/watch?v=US8QG9I1XW0) for "good architecture" and scalability. I'm a big advocate for [PORTS AND ADAPTERS](../patterns/ports_and_adapters.md) as a default architecture, but the problem is that it requires knowledge and discipline to maintain and architecture like that. As Mark Seemann mentions in the previously linked talk, it feels like trying to balance a boulder on a hill. Instead, we want to use tools that naturally lead to these kinds of architectures (or at least lead to similar outcomes) -- in his talk he gives the example of how Haskell requiring functional purity leads to code that looks a lot like ports and adapters, but without all the reading and discipline. In fact the language more or less enforces it.

Kotlin does not have the benefit of being a "pure" language, but it does have a really excellent async/coroutine syntax that can be applied as a pastiche of pure languages. See this excellent [explanation on how to enforce function purity at compile time](https://arrow-kt.io/docs/fx/purity-and-referentially-transparent-functions/) by the Arrow team.

Unfortunately, one could easily get around this by simply not marking impure functions with `suspend`. Though in my experience, with buy-in from the team, this approach works pretty well. Most importantly, it works way way way better than just asking people to try to separate pure and impure functions. And it's miles better than askign everyone to respect the ports and adapters architecture and to read some architecture books (though everyone should anyway, if they have the time).

But even in Haskell, there is some discontent about using `IO` to manage impure functions, and you can see a bit of that problem with `suspend`. That is, it lets you know the function is impure but in what way is it impure? Does it throw exceptions? Is it non deterministic? Does it depend on external state? It would help out with debugging and maintenance if we could tell these things without needing to dig deep into the implementation.

That's where Algebraic Effects come into play! My brain is a little bit too smooth to fully understand all of the applications of this idea, but my takeaway is: tracking effects in the type signature! That means knowing a function is impure based on its type signatue AND knowing what effects it causes. And even more than that, it provides another flavor of dependency injection that, in my opinion, is a more ergonomic flavor of ports and adapters.

Plus, the big whitepaper is called ["Do Be Do Be Do"](https://arxiv.org/pdf/1611.09259.pdf) which should just automatically make everyone a fan.

The Unison language has a cool implementation of algebraic effects (in addition to a lot of other incredibly cool ideas) but if you want to check that out some more you can look at [their website](https://www.unisonweb.org/) or [a blog post I wrote using algebraic effects in a common ports and adapters code kata.](https://www.unisonweb.org/2021/08/20/birthday-kata/).

Similarly, there are several Haskell libraries attempting to add similar functionality (though they aren't as nice as the built in Unison support for it). Check out [another great blog post](https://reasonablypolymorphic.com/blog/freer-monads/) by Sandy Maguire on using Freer Monads in Haskell.

All of this is lead up to say that the new context receivers prototype in 1.6.2 gives us a taste of this functionality in Kotlin. And the result is... it's really nice!

I decided to play around with this prototype in a silly little Wordle clone (I'm hip to what the kids are doing!).

Please take a look at the project and feel free to clone it and play around with it! I've also included more thoughts about the actual code in the [project's README.](wordle_with_kotlin_context/README.md)
