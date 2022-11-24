package lectures.part5TypeSystem

object PathDependentTypes extends App{

  class Outer {
    class Inner
    object InnerObject
    type InnerType
  }

  def aMethod: Int ={
    class HelperClass
    type HelperType = String // have to use alias when outside of classes and traits
    2
  }

  val o = new Outer
  val inner = new o.Inner  // o.inner is its own type

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K

  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  //def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  //get[IntItem](42)
  //get[StringItem]("scala")


}
