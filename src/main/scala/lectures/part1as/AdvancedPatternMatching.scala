package lectures.part1as

import java.security.KeyStore.TrustedCertificateEntry

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ =>
  }

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21 ) "minor" else "major")
  }

  val bob = new Person("Bob", 25)

  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a yo"
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"

  }

  print(legalStatus)

  object even {
    def unapply(arg: Int): Option[Boolean] =
      if (arg%2 == 0) Some(true)
      else None

  }

  val n: Int = 45
  val mathProperty = n match {
    case x if x < 10 => "Single digit"
    case x if x %2 == 0 => "Even Number"
    case _ => "No property"
  }

  // Infix patterns
  case class Or[A, B](a: A, b: B)
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"
  }

  println(humanDescription)




}