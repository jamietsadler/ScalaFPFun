package lectures.part4implicits
import java.util.Date

object JSONSerialisation extends App{

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    def stringify: String = "\"" + value + "\""

  }
  final case class JSONNumber(value: Int) extends JSONValue{
    def stringify: String = value.toString

  }

  final case class JSONAray(values: List[JSONValue]) extends JSONValue {
    def stringify: String = values.map(_.stringify).mkString("[", ",", "]")

  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    def stringify: String = values.map {
      case (key, value) => "\"" + key + "\"" + value.stringify
    }
      .mkString("{", ",", "}")
  }

  val data = JSONObject(Map(
    "user" -> JSONString("Jamie"),
    "posts" -> JSONAray(List(JSONString("asdas"), JSONNumber(345)))
  ))

  println(data.stringify)

  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  implicit object StringConverter extends JSONConverter[String] {
    def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    def convert(value: Int): JSONValue = JSONNumber(value)
  }

  implicit object UserConverter extends JSONConverter[User] {
    def convert(user: User): JSONValue =JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "created" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> UserConverter.convert(feed.user),
      "posts" -> JSONAray(feed.posts.map(PostConverter.convert))
    ))
  }

  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  val now = new Date(System.currentTimeMillis())
  val john = new User("John", 20, "john@sdfsdf.com")
  val feed = Feed(john, List(
    Post("hello", now),
    Post("look at this dog", now)
  ))

  println(feed.toJSON.stringify)

}
