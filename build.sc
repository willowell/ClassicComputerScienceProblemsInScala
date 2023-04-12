import mill._, scalalib._

object versions {
  def scala = "3.2.2"
}

object ScalacOptions {
  lazy val compile: Seq[String] =
    Seq(
      "-deprecation",                  // Emit warning and location for usages of deprecated APIs.
      "-encoding", "utf-8",            // Specify character encoding used by source files.
      "-feature",                      // Emit warning and location for usages of features that should be imported explicitly.
      "-language:existentials",        // Existential types (besides wildcard types) can be written and inferred
      "-language:experimental.macros", // Allow macro definition (besides implementation and application)
      "-language:higherKinds",         // Allow higher-kinded types
      "-language:implicitConversions", // Allow definition of implicit functions called views
      "-language:unsafeNulls",
      "-unchecked",                    // Enable additional warnings where generated code depends on assumptions.
      "-Xfatal-warnings",              // Fail the compilation if there are any warnings.
      "-language:strictEquality",      // Scala 3 - Multiversal Equality, https://docs.scala-lang.org/scala3/book/ca-multiversal-equality.html
      "-Yexplicit-nulls",              // Null can only be assigned to types which may be null; e.g., `val x: Int = null` is an error - https://docs.scala-lang.org/scala3/reference/experimental/explicit-nulls.html
      "-Ysafe-init"                    // https://docs.scala-lang.org/scala3/reference/other-new-features/safe-initialization.html
    )

  lazy val test: Seq[String] =
    Seq(
      "-deprecation",                  // Emit warning and location for usages of deprecated APIs.
      "-encoding", "utf-8",                         // Specify character encoding used by source files.
      "-feature",                      // Emit warning and location for usages of features that should be imported explicitly.
      "-language:existentials",        // Existential types (besides wildcard types) can be written and inferred
      "-language:experimental.macros", // Allow macro definition (besides implementation and application)
      "-language:higherKinds",         // Allow higher-kinded types
      "-language:implicitConversions", // Allow definition of implicit functions called views
      "-unchecked",                     // Enable additional warnings where generated code depends on assumptions.
      "-Yexplicit-nulls",
      "-Ysafe-init"
    )
}

trait BookModule extends ScalaModule {
  override def scalaVersion = versions.scala
  override def scalacOptions = ScalacOptions.compile
  override def moduleDeps = Seq(core)
}

object book extends BookModule {
  object chapter01 extends ScalaModule {
    /* Fibonacci Sequence Examples */
    object fib1 extends BookModule
    object fib2 extends BookModule
    object fib3 extends BookModule
    object fib4 extends BookModule
    object fib5 extends BookModule
    object fib6 extends BookModule

    object bitVectors extends BookModule

    override def scalaVersion = versions.scala
    override def scalacOptions = ScalacOptions.compile
    override def moduleDeps = Seq(core, fib2)
  }
}

object core extends ScalaModule {
  override def scalaVersion = versions.scala

  override def scalacOptions = ScalacOptions.compile
}
