package lectures.part2afp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 5
    else throw new FunctionNotApplicable

  class FunctionNotApplicable extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match { // Proper function
    case 1 => 42
    case 2 => 5
    case 5 => 98
  }

  val aPartialFunction: PartialFunction[Int, Int] = { // partial function, can only be assigned to PartialFunction
    case 1 => 42
    case 2 => 5
    case 5 => 98
  }

  println(aPartialFunction(2))
  //println(aPartialFunction(3))

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  // PFs can be lifted to total functions returning options
  val lifted = aPartialFunction.lift // Int => Option[Int]

  println(lifted(2))
  println(lifted(4))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }

  println(aMappedList)

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "My nae is asdasdas"
    case "goodbye" => "asdasdasda"
    case "blah" => "Ok"
  }

  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)

}