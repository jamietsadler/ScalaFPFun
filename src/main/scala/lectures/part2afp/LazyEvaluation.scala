package lecture.part2afp

object LazyEvaluation extends App {

  lazy val x: Int = throw new RuntimeException // lazy delays evaluation of values
  //println(x)

  lazy val y: Int = {
    println("Hello")
    42
  }

  println(y)
  print(y) // won't evaluate again

  // examples of implications
  def sideEffectCondition: Boolean = {
    println("boooo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyConditiin = sideEffectCondition

  println(if (simpleCondition && lazyConditiin) "yes" else "no")


  def byNameMethod(n: => Int): Int = n + n + n + 1

  def retrieveMagicValue: Int = {
    Thread.sleep(1000)
    println("waiting")
    42
  }

  println(byNameMethod(retrieveMagicValue))

  // Use lazy vals
  def byNameMethodLazy(n: => Int): Int = {
    lazy val t: Int = n // useful when side effects
    t + t + t + 1
  }

  // Call by need
  println(byNameMethodLazy(retrieveMagicValue))

  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greather than 20")
    i > 20
  }

  val numbers = List(1, 25, 24, 25, 26, 27, 50, 23, 40)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)
  println(lt30)
  println("--------")
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30)
  val gt20Lazy = lt30.withFilter(greaterThan20) // no side effects

  gt20Lazy.foreach(println)

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B]
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B]

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes first n elements out of stream
    def takeAsList(n: Int): List[A]
    }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[B] = ???


  }

}