/// # Evaluation Strategies in Scala 3
///
///
package book.chapter00.evaluationStrategies

class Foo(var x: Int)

class Bar(val x: Int)

@main def run(): Unit = {
  //==================================================================================================================//
  // `val`: Immutable Bindings                                                                                        //
  //==================================================================================================================//
  
  val immX = 42

  // Compiler error!
  //immX = immX / 2

  val foo = Foo(21)

  // Compiler error!
  //foo = Foo(42)

  // OK, `x` class member is mutable
  foo.x = foo.x * 2

  val bar = Bar(21)

  // Compiler error!
  //bar = Bar(42)

  // Compiler error!
  //bar.x = bar.x * 2

  //==================================================================================================================//
  // `var`: Mutable Bindings                                                                                          //
  //==================================================================================================================//
  
  var mutX = 21

  mutX = 21 * 2

  //==================================================================================================================//
  // `def`: Functions and Call-by-Name Bindings                                                                       //
  //==================================================================================================================//

  // Inferred with type => Int AKA call-by-name Int
  // Like any function, this value will be recreated and returned each time it is called
  // i.e., `42` is not stored as it is in a `val` or `var`.
  def defX = 42

  // Inferred with Int return type
  def defX2() = 42

  //==================================================================================================================//
  // `lazy val`: Lazy Immutable Bindings                                                                              //
  //==================================================================================================================//

  // `lazy val`s are only computed once you do something with them. In the meantime, they are kept as thunks - little I.O.U. boxes
  // with promises to compute the value when they're needed.
  // Afterwards, they are kept in memory like a `val`.
  // Among other things, this enables infinite lists and recursive `val`s, as shown in `fib5.scala`, `fib6.scala`, and `fib7.scala`.

  // Scala (and Haskell!) require `lazy val`s to be immutable to ensure that these values do NOT need to be calculated immediately.
  // If `lazy` values are allowed to be mutable, no guarantee can be made that once a thunk is completed, that value will not change,
  // or that the thunk itself will promise the same value over its lifetime.
  // For instance, what would it mean to modify a value of a lazy infinite list?

  lazy val lazyX = 42

  // Compiler error: "lazy not allowed here. Only vals can be lazy"
  //lazy var lazyY = 42
}
