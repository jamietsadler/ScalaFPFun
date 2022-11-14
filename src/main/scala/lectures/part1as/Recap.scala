package lectures.part1as
import scala.annotation.tailrec

object Recap extends App {
  val aCondition: Boolean = false
  val aConditionalVal = if (aCondition) 42 else 64

  // Instructions vs Expressions
  val aCodeBlock = if (aCondition) 54
  else 56

  // Unit (void)
  val theUnit = println("Hello World")

  // tailrec (Dont use additional stack frames, converted to iterations in JVM byte code).

  @tailrec def factorial(n: Int, acc: Int): Int =
    if ( n <= 0) acc
    else factorial(n-1, n*acc)

  class Animal
  class Dog extends Animal

  val aDog: Animal = new Dog // subtype polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("Crunch")
  }

  // method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog // very expressive

  // 1 + 2 == 1.(2) many expressions get rewrtten into methods

  // anonymous classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("Roar")
  }

  // generics
  abstract class MyList[+A] // variance

  object MyList // Companion

  // Case classes
  case class Person(name: String, age: Int)

  // exceptions
  val throwsException = throw new RuntimeException
  val aPotentialFailure = try
    throw new RuntimeException
  catch
    case e: Exception => "An exception"
  finally
    println("Some logs")


  // fucntional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1

  }

  incrementer(1)

  val anonymousIncrementer = (x: Int) => x + 1

  List(1, 2, 3).map(anonymousIncrementer) // HOF

  val pairs = for {
    num <- List(1, 2, 3)
    char <- List('a', 'b', 'c')
  } yield num + "-" + char

  // Collections
  val aMap = Map(
    "James" -> 2234234,
    "asdasd" -> 12312
  )

  val anOption = Some(2)

  // Pattern Matching
  val x: Int = 2

  val order = x match {
    case 1 => "First"
    case 2 => "Second"
    case 3 => "Third"
    case _ => x + "th"
  }

  val Bob = Person("Bob", 22)
  val greeting = Bob match {
    case Person(n, _) => s"Hi my name is $n"
  }

  println(greeting)



}