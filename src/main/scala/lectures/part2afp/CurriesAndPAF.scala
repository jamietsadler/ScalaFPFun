package lecture.pastafp

object CurriesAndPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5))

  // curried because receives multiple parameter lists
  // methods are part of classes or singleton objects,
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4) // Need annotations, otherwise compiler doesn't now what result should be

  // lifting - ETA EXPANSION
  // Functions != methods

  def inc(x: Int): Int = x + 1
  List(1, 2, 3).map(inc) // ETA-expansion

  // Partial Function Applications
  val add5 = curriedAdder(5) _ // Int => Int without the annotations

  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  val add7 = (x: Int) => simpleAddFunction(7, x) // simplest
  val add7_2 = simpleAddFunction.curried(7)

  val add7_3 = curriedAddMethod(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_) // Also PAF

  val add7_5 = simpleAddMethod(7, _: Int) // alt syntax for turning methods into function values
            // Compiler turns into y => simpleAddMethod(7, y)

  // underscores are powerful
  def concatenator(a: String, b: String, c: String): String = a + b + c
  val insertName = concatenator("Hello I'm ", _: String, ", how are you?") // ETA expansion
  println(insertName("Jamie"))

  val fillIntTheBlanks = concatenator("Hello ", _: String, _: String) // (x, y) => concatenator("Hello, ", x, y)
  println(fillIntTheBlanks("Jamie", " asdasd"))

  def byName(n: => Int): Int = n + 1
  def byFunction(f: () => Int): Int = f() + 1
  def method: Int = 42
  def parenMethod(): Int = 42

  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  // curried functions allow methods to be sued in number of ways
  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(simpleFormat))
  println(numbers.map(preciseFormat))

  byName(23) // ok
  byName(method)



}