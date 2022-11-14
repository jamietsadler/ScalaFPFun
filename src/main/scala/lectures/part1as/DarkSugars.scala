package lectures.part1as

import scala.annotation.targetName

object DarkSugars extends App {
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  // can supply args with curly braces in syntax similar to java
  val description = singleArgMethod {
    42
  }

  /**
  val aTryInstance = try { // looks like java
        throw new RuntimeException
  }
  */

  List(1, 2, 3).map { x =>
      x + 1
  }

  // syntax sugar 2
  // Instance with single method can be reduced to lambdas

  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello scala")
  })

  val aSweeterThread = new Thread(() => println("sweet, scala"))

  abstract class AnAbstractType {
    def implemented: Int = 25
    def f(a: Int): Unit
  }

  val anAbstractTInstance: AnAbstractType = (a: Int) => println("sweet")

  // syntax sugar 3: :: and #::
  val preprendedList = 2 :: List(2, 3)
  // scala specificity: last char decided associativity of method

  // Syntax sugar 4: mulitword method naming

  class GossipGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }

  val lilly = new GossipGirl("jj")
  lilly `and then said` "scala is so sweet"

  // Syntax sugar 5: infix types

  class Composite[A, B]

  val composite: Composite[Int, String] = ???
  val composite2: Int Composite String = ???

  @targetName("Test")
  class -->[A, B]
  val towards: Int --> String = ???

  // Syntax sugar 6: update() os special method

  val anArray = Array(1, 2, 3)

  anArray(2) = 7 // rewritten to array.update
  // used in mutable collections






}