# Classic Computer Science Problems in Scala

Scala 3 port of David Kopec's "Classic Computer Science Problems" series, based on a fusion of his Java and Swift books.

Rather than being a plain Java-to-Scala transliteration, this implementation explores Scala's fusion of FP and OOP.

I'm also using this as a way to learn Scala, using the exercises at bite-size lessons!

## Prerequisites

In order to work with this codebase, you need:

* The Scala 3 toolchain, preferably installed via Coursier as described on the Scala website.
* Scala-CLI, which you may already have installed via Coursier above!

### Why Scala CLI?

Like [the *Functional Programming in Scala* repository](https://github.com/fpinscala/fpinscala), Scala CLI can handle straightforward Scala projects like this one.

`project.scala` tells Scala-CLI where the project root directory is, as well as tells Scala CLI about global settings and dependencies.

## Examples

The examples are all under `/src/book/`, organized into chapters.

Each example includes a short comment overview with a link to the Java implementation.

For examples specific to this port such as `/src/book/chapter01/fib6.scala`, there is obviously no link to a corresponding Java implementation.

Keep in mind that some examples are intentionally slow, like the naive fibonacci function implementation.

## How to run the examples

Much like the Java implementation, each example is its own program entry. However, I am using the `/src/core/` directory as a common library, not unlike the shared `/Sources` directory in the Swift Playground version.

To run the examples, please follow these directions:

1. Check the structure of the `/src/book/` directory. Make a note of the names of the nested objects.
2. Open a terminal and run `scala-cli run --main.class book.chapter<number>.<name>.run`. For instance, `scala-cli run --main-class book.chapter01.fib2.run`.
3. Scala-CLI will take a couple moments to to start the Bloop build server, collect dependencies, and then run the program.
4. That's it! If all goes well, you'll see the program's output in your terminal.

> :warning: Don't forget to add the `.run` part!
> That part corresponds to the main class.
> I prefer using `@main def run(): Unit = { ... }` throughout most of the book.
> For examples using Cats Effect, you may see something else! (TODO - fill this out when I get to examples using CE)

### Use Scala-CLI to Find Main Classes

You can use Scala-CLI to find available main classes in this project. Just run `scala-cli run .` in the root directory!

Scala-CLI will then print out a list of available main classes, along with an example and more info on how to run them.

## How to interact with this project through the Scala REPL

Much like how you can explore the Swift implementation using a Swift Playground in Xcode, you can explore this whole project using the Scala REPL. Just run `scala-cli repl .` in the root directory - you will then have a Scala REPL in the context of this project.

You can then import and explore any package in this project, like so:

```
➜ scala-cli repl .
Welcome to Scala 3.3.0 (19, Java OpenJDK 64-Bit Server VM).
Type in expressions for evaluation. Or try :help.

scala> import core.text.prettyprinting.*

scala> true.toYesOrNo
val res0: String = yes
```

## Assumptions

I am assuming:

* You have downloaded and installed the Scala toolchain via Coursier and verified that it is working.
* You have downloaded and installed Scala-CLI and verified that it is working. (You might already have it via Coursier!)
* You are using a platform Scala readily supports.
* You are somewhat familiar with Scala and its standard library.

## External dependencies

As you can see in `project.scala`, this port includes external dependencies.

Kopec makes an excellent point of using only the Java standard library in his *Classic Computer Science Problems in Java* book - as any Node.js developer no doubt fully understands, external dependencies introduce entropy and maintainance costs into an otherwise self-contained program. Before long, you're sitting on a > 500 MB `node_modules` folder.

At some point, some minor update or misconfiguration will result in the program failing in some way!



## How this repository is organized

This repository consists of a `/src/core/` directory that holds shared code and a `/src/book/` directory that consists of binary entrypoints which may or may not use code from the `/src/core/` directory.

Every `.scala` file in `/src/book/` has a main class (i.e., is a binary entrypoint).

This implementation sits somewhere between the Swift and Java implementations - the shared `/src/core/` directory is not unlike the shared `Sources` in the Swift Playground implementation, but this implementation uses ostensibly standalone entrypoints in `/src/book/`.

## Extra content!

This port includes content beyond what the Java implementation includes, such as new chapters and Scala-specific examples.

### New chapters!

For instance, Chapter Ex 1 (`/src/book/chapterEx/`) covers the Expression Problem and all the ways one can approach it in Scala. Chapter Ex 2 (`/src/book/chapterEx2/`) covers concurrent problems, using [Cats Effect](https://typelevel.org/cats-effect/) to make them accessible and approachable!

Chapter 0 (`/src/book/chapter00/`) covers some Scala concepts and code conventions I use that you may not be familiar with. Scala is infamous for [TIMTOWTDI](https://wiki.c2.com/?ThereIsMoreThanOneWayToDoIt), so part of this chapter's job is to establish code conventions I use throughout the book, so you know what to expect going forward.

### New examples in existing chapters!

Where possible, this port includes Scala-specific examples, such as `fib6.scala` and `fib7.scala`, which explore creating an infinite list of fibonacci numbers using Scala's `LazyList` and fs2's `Stream` respectively.

These extra examples serve a few purposes:

* To introduce / explore concepts used elsewhere in the chapter. For instance, `/src/book/chapter02/pq.scala`, which is an exploration of Scala's `PriorityQueue`.
* To offer a different perspective! For instance, since Scala supports `LazyList`s, I can build on Kopec's fibonacci examples with an example that is not (currently) possible in vanilla Java.

## License

This repository is licensed under the GNU Affero General Public License Version 3 or later.

As Scala may be transpiled to JavaScript using Scala.js, it is possible to use this repository as a web service; for instance, as the backend or part of a backend for a web visualization or on the frontend.

As such, I have applied the AGPL to ensure any changes and/or improvements to this repository are made public in kind.
