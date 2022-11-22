package lectures.part4implicits

object OrganisingImplicits extends App{


  //println(List(1, 3, 2, 6, 4, 2).sorted)
  // implicit ordering value
  // comes from scala.Predef

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  // scala can get confused and not know which implicit val to use.

  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Tom", 21),
    Person("Peter", 55)
  )


  implicit val alphabetOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name ) < 0)

  println(persons.sorted)

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }  


}
