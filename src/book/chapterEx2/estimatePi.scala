/// # Estimate pi concurrently
///
/// This is a spin on the "Calculate Pi" example from Chapter 1, inspired by the "Pi estimation" example from
/// the Spark docs: https://spark.apache.org/examples.html
///
/// In this example, we estimate pi by randomly creating points between (0, 0) and (1, 1) and figuring out how many
/// fall inside the unit circle. As the Spark page linked above says, the fraction should be pi / 4, so we can
/// multiply the fraction by 4 to get our approximation of pi.
///
package book.chapterEx2.estimatePi

import cats.effect.*
import cats.syntax.all.*
import cats.effect.std.Random

case class Point(x: Double, y: Double)

def genRandomPointInUnitCircle(gen: Random[IO]): IO[Point] = for {
  x <- gen.nextDouble
  y <- gen.nextDouble
} yield (Point(x, y))

def isPointInUnitCircle(p: Point): Boolean = (p.x * p.x + p.y * p.y) < 1

object Main extends IOApp.Simple {
  final val NUM_SAMPLES = 1000

  override final val run: IO[Unit] = for {
    gen <- Random.scalaUtilRandom[IO]

    xs = (1 to NUM_SAMPLES).toList

    ps <- xs.parTraverse { _ => genRandomPointInUnitCircle(gen) }

    ys = ps.filter { isPointInUnitCircle }

    count = ys.length

    _ <- IO.println(s"Pi is roughly ${4.0 * count / NUM_SAMPLES}")
  } yield ()
}
