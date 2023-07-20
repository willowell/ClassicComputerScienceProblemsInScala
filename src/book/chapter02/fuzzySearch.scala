/// # Fuzzy Search
///
/// 
///
/// https://gist.github.com/tixxit/1246894/e79fa9fbeda695b9e8a6a5d858b61ec42c7a367d
///
/// https://github.com/vickumar1981/stringdistance/blob/master/src/main/scala/com/github/vickumar1981/stringdistance/impl/LevenshteinDistanceImpl.scala
///
package book.chapter02.fuzzySearch

import scala.math.Integral

def levenshteinDist[A](a: Iterable[A], b: Iterable[A]): Int = {
  import scala.math.{max, min}
  
  a
    .foldLeft((0 to b.size).toList) { (prev, x) => {
      (prev.zip(prev.tail).zip(b))
        .scanLeft(prev.head + 1) {
          case (h, ((d, v), y)) => {
            min(
              min(h + 1, v + 1),
              d + (if (x == y) 0 else 1)
            )
          }
        }
      }
    }.last
}

def score[A](a: Seq[A], b: Seq[A]): Double = {
  import scala.math.{max, min}

  val maxLen = max(a.length, b.length)
  val minLen = maxLen - levenshteinDist(a, b)

  val numerator: Double = if minLen < 0 || minLen > maxLen
    then 0.0
    else minLen * 1.0

  numerator / maxLen
}

case class SearchResult(
  term: String,
  score: Double,
  distance: Int,
)

val orderByScore: Ordering[SearchResult] = Ordering.by({ (sr: SearchResult) => sr.score }).reverse

def fuzzySearch(xs: List[String])(term: String, limit: Int = 10): List[SearchResult] = {
  val scored = for {
    x <- xs
    s = score(term, x)
    d = levenshteinDist(term, x)
  } yield SearchResult(x, s, d)

  scored.sorted(using orderByScore)
}

@main def run(): Unit = {
  val terms = List(
    "Hi",    "Hola",  "Bonjour",   "Hello",
    "Hallo", "Salut", "Greetings", "Salutations",
  )

  def search = fuzzySearch(terms)(_)

  val res = search("Hello")

  println("Got search results:")

  for (SearchResult(term, termScore, dist) <- res) {
    val str = s"\"$term\""

    println(f"$str%-20s => score $termScore%.2f, distance $dist")
  }
}
