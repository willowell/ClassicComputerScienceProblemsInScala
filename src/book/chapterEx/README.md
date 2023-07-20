# The Expression Problem

The Expression Problem is a classic Computer Science problem about extending a closed API.

## Problem Statement

## About

Usually associated with programming language design, this problem can appear when you least expect it, and in many ways gets at the heart of some of the most interesting challenges we face as computer programmers: how do you create an API that is *flexible, extensible, and maintainable*?

How do you achieve one or more of these qualities if some portion of the API is outside your control? How do you abstract over the API to shield yourself from upstream changes?

Scala enables us to explore the Expression Problem from multiple angles:

* A traditional, Visitor-based OOP approach
* A traditional, ADT-based FP approach
* A fusion, using Scala's support for extending classes
* An algebraic, Free Monad-based approach

## Chapter Organization

This chapter contains a folder for each approach to the Expression Problem.

In each approach, I explore creating a simple AST for a calculator that supports addition, subtraction, multiplication, division, and grouping. The AST includes a leaf for a double literal as well.

Each approach includes a primary interpreter that evaluates the AST.

To expand on the approach, I either:

* Add a new interpreter that prints the AST.
* Add a new modulo expression.

In each approach, you will find:

* `initial.scala` - this is the starting place that follows the approach.
* `newExpression.scala` - this expands on `initial.scala` with a new expression, with modified lines highlighted.
* `newInterpreter.scala` - this expands on `initial.scala` with a new function / operation / interpreter, with modified lines highlighted.

## Grading Solutions

Based on the modifications demonstrated above, I will grade each solution according to this system:

* "Not a Solution" - the approach requires modification across one or more dimensions.
* "Partial Solution" / "Solution with respect to FP / OOP" - the approach solves the primary limitation of the paradigm but introduces new issues (e.g., as in the Visitor Pattern approach)
* "Complete Solution" - the approach allows extension across both dimensions.

In terms of code, a "Complete Solution" should present the AST and its interpreters in a fairly loose, ad-hoc fashion. i.e., adding a new expression or a new interpreter should require *only adding new code*.

## The Answer is Multiple Dispatch

No matter what, our goal is essentially to implement multiple dispatch in a way that gives us control over extending the types and/or behaviours that the system can operate on.

## Further Reading
