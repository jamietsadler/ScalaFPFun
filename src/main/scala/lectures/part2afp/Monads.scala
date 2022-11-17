package lecture.pastafp

object Monads extends App {


  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }

  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = ???
  }

  class Lazy[+A](value: => A) { // call by name (need) prevents value from being evaluated when object being constructed
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(value)
    def use: A = value
    private lazy val internalValue = value
    }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I dont feel like doing anythiung")
    42
  }

  println(lazyInstance.use)
  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10*x
  })



}