package lectures.part4implicits

object PimpingLibraries extends App{
  //
  implicit class RichInt(val value: Int) extends AnyVal{
    def isEVen: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] = {
        if (n <= 0) List()
        else concatenate(n - 1) ++ list
      }
        concatenate(value)
    }

  }

  new RichInt(43).sqrt

  42.isEVen // type enrichment (pimping)
  // rewrites as new RichInt(42).isEven


  1 to 10

  import scala.concurrent.duration._
  3.seconds

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }

  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string)
    def encrypt(cyperDistance: Int): String = string.map(c => (c + cyperDistance).asInstanceOf[Char])
  }

  println("2".asInt + 4)
  println("John".encrypt(4))

  println(4*List(1, 2))


}
