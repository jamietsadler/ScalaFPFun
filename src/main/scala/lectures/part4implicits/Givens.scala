package lectures.part4implicits

object Givens extends App{

  val aList = List(4,3,1,3,7,3)
  val orderedList = aList.sorted

  // scala 2 stsyle
  object implicits {
    implicit val descendOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }

  // scala 3:
  // implicits becoming deprecated
  // solution is givens
  object Givens {
    given descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }
  // Givens (given instances) are equivalent to given vals

  object GivenAnonymousClassNaive {
    given descendingOrdering_v2: Ordering[Int] = new Ordering[Int] {
      override def compare(x: Int, y: Int) = y - x
    }
  }

  object GivenWith {
    given descendingOrdering_v3: Ordering[Int] with {
      override def compare(x: Int, y: Int) = y - x
    }
  }

  import GivenWith._ // have to import givens explicitly in Scala 3
  import GivenWith.descendingOrdering_v3
  import GivenWith.given // import all givens

  def extremes[A](list: List[A])(implicit ordering: Ordering[A]): (A, A) = {
    val sortedList = list.sorted
    (sortedList.head, sortedList.last)
  }

  def extremes_v2[A](list: List[A])(using ordering: Ordering[A]): (A, A) = {
    val sortedList = list.sorted // (compiler injects ordering)
    (sortedList.head, sortedList.last)
  }

  trait Combinator[A] { // semigroup
    def combine(x: A, y: A): A
  }

  // scala 2
  implicit def listOrdering[A](implicit simpleOrdering: Ordering[A], combinator: Combinator[A]): Ordering[List[A]] =
    new Ordering[List[A]] {
      override def compare(x: List[A], y: List[A]) = {
        val sumX = x.reduce(combinator.combine)
        val sumY = y.reduce(combinator.combine)
        simpleOrdering.compare(sumX, sumY)
      }
    }


  // scala 3
  given listOrdering_v2[A](using simpleOrdering: Ordering[A], combinator: Combinator[A]): Ordering[List[A]] with {
    override def compare(x: List[A], y: List[A]) = {
      val sumX = x.reduce(combinator.combine)
      val sumY = y.reduce(combinator.combine)
      simpleOrdering.compare(sumX, sumY)
    }

  }

  case class Person(name: String) {
    def greet(): String = s"Hi, my name is $name"
  }

  import scala.language.implicitConversions // required in scala 3
  given String2PersonConversion: Conversion[String, Person] with {
    override def apply(x: String) = Person(x)
  }

}
