/// # Fibonacci with infinite recursion
///
/// This fibonacci program will never terminate!
/// You've been warned. Rust's compiler doesn't like this program either!
///
/// Java implementation: https://github.com/davecom/ClassicComputerScienceProblemsInJava/blob/master/CCSPiJ/src/chapter1/Fib1.java
///
package book.chapter01.fib1

def fib(x: Int): Int = fib(x - 1) + fib(x - 2)

@main def run(): Unit = {
  println("I will run forever!")

  val res = fib(20)

  println(s"Result: $res")
}
