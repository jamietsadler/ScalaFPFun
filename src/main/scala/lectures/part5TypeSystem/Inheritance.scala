package lectures.part5TypeSystem

object Inheritance extends App{

  // convenience
  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closeable {
    def close(status: Int): Unit
  }

  trait GenericStream[T] {
    def foreach(f: T => Unit): Unit
  }

  def ProcessStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // Diamond Problem
  trait Animal { def name: String }
  trait Lion extends Animal { override def name: String = "lion" }
  trait Tiger extends Animal{ override def name: String = "tiger" }

  class Mutant extends Lion with Tiger

  val m = new Mutant
  println(m.name) // last override always gets picked

  // the super problem + type linearisation

  trait Cold {
    def print: Unit = println("cold")
  }

  trait Green extends Cold {
    override def print: Unit = {
      println("Green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("Blue")
      super.print
    }
  }

  class Red {
    def print: Unit = println("Red")
  }

  class White extends Red with Green with Blue{
    override def print: Unit = {
      println("White")
      super.print
    }
  }

  val col = new White
  println(col.print)


}
