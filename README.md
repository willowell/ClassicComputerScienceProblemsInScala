# Classic Computer Science Problems in Scala

Scala 3 port of David Kopec's "Classic Computer Science Problems" series, based on a fusion of his Java and Swift books.

Rather than being a plain Java-to-Scala transliteration, this implementation explores Scala's fusion of FP and OOP.

I'm also using this as a way to learn Scala, using the exercises at bite-size lessons!

## Prerequisites

In order to work with this codebase, you need:

* The Scala 3 toolchain, preferably installed via Coursier as described on the Scala website.
* The Mill build tool, installed following the directions on the Mill website.

### Why Mill and not SBT?

Basically because Mill is much easier to understand. Take a look at the `build.sc` Mill build script - you will see what I mean!

## Examples

The examples are all under `/book`, organized into chapters.

Each example includes a short doc-comment overview with a link to the Java implementation.

For original examples such as `/book/chapter01/fib6.scala`, there is obviously no link to a corresponding Java implementation.

Keep in mind that some examples are intentionally slow, like the naive fibonacci function implementation.

## How to run the examples

Much like the Java implementation, each example is its own program entry. However, I am using the `/core` directory as a common library, not unlike the shared `/Sources` directory in the Swift Playground version.

To run the examples, please follow these directions:

1. Check the structure of the `book` object in `build.sc`. Make a note of the names of the nested objects.
2. Open a terminal and run `mill book.chapter<number>.<name>.run`. For instance, `mill book.chapter01.fib1.run`.
3. Mill will take a couple moments to collect dependencies, and then run the program.
4. That's it! If all goes well, you'll see the program's output in your terminal.

## Assumptions

I am assuming:

* You have downloaded and installed the Scala toolchain via Coursier and verified that it is working.
* You have downloaded and installed the Mill build tool and verified that it is working.
* You are using a platform Scala readily supports.
* You are somewhat familiar with Scala and its standard library.

For the most part, this implementation does not dive into advanced features in Scala.



## How this Repository is Organized

This repository consists of a `/core` directory that holds shared code and a `/book` directory that consists of binary entrypoints which may or may not use code from the `/core` directory.

This implementation sits somewhere between the Swift and Java implementations - the shared `/core` directory is not unlike the shared `Sources` in the Swift Playground implementation, but this implementation uses ostensibly standalone entrypoints.

## License

This repository is licensed under the GNU Affero General Public License Version 3 or later.

As Scala may be transpiled to JavaScript using Scala.js, it is possible to use this repository as a web service; for instance, as the backend or part of a backend for a web visualization or on the frontend.

As such, I have applied the AGPL to ensure any changes and/or improvements to this repository are made public in kind.
