package core.data.time

def time[R](block: => R): R = {
  import java.time.{Duration, Instant}

  val t0 = Instant.now

  val result = block

  val t1 = Instant.now

  println(s"Elapsed: ${Duration.between(t0, t1).nn.toMillis} ms")

  result
}
