package lectures.part4implicits
import lectures.part4implicits.TypeClasses.HTMLSerialiserPM

import java.util.Date

object TypeClasses extends App{

  trait HTMLWritable{
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div> $name and $age <a href=$email/> </div>"
  }

  val john = User("John", 32, "john@html.com").toHtml

  object HTMLSerialiserPM {
    def serialiseToHtml(value: Any): Unit = value match {
      case User(n, a, e) =>
      case _ =>
    }
  }

  trait HTMLSerialiser[T] { // type class
    def serialise(value: T): String
  }

  implicit object UserSerialiser extends HTMLSerialiser[User]{
    def serialise(user: User): String = s"<div> ${user.name} and ${user.age} <a href=${user.email}/> </div>" // type class instances
  }

  object DateSerialiser extends HTMLSerialiser[Date] {
    override def serialise(date: Date): String = s"<div>${date.toString()}"
  }

  object PartialUserserialiser extends HTMLSerialiser[User] {
    def serialise(user: User): String = s"<div> ${user.name} and ${user.age} <a href=${user.email}/> </div>"
  }

  // TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b:User): Boolean = a.name == b.name && a.email == b.email
  }

  /*object HTMLSerialiser {
    def serialise[T](value: T)(implicit serialiser: HTMLSerialiser[T]): String =
      serialiser.serialise(value)
  }

  object IntSerialiser extends HTMLSerialiser[Int] {
    override def serialise(value: Int): String = s"<div style:color = blue.$value</div>"
  }

  println(HTMLSerialiser.serialise(42))
  */




}
