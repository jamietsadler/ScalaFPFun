package lectures.part5TypeSystem

object SelfTypes extends App{

  // way of requiring type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist => // impmementing Singer will also require implementing instrumenalist
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  val jamesHetfield  = new Singer with Instrumentalist:
    override def sing(): Unit = ???
    override def play(): Unit = ???

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("(guitar solo)")
  }

  val ericClapton = new Guitarist with Singer:
    override def sing(): Unit = ???


  class A
  class B extends A // B is an A

  trait T
  trait S { self: T => } // S requires a T

}
