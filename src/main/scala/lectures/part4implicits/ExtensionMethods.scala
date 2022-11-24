package lectures.part4implicits

object ExtensionMethods extends App{

  case class Person(name: String) {
    def greet(): String = s"Hi Im $name"
  }

  extension (string: String) {
    def greetAsPerson(): String = Person(string).greet()
  }

  val jamieGreeting = "Jamie".greetAsPerson()

  // extension methods are the same as implicit class

}
