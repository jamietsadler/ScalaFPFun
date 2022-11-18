package lectures.part3concurrency

import scala.concurrent.{Future, Await, Promise}
import scala.concurrent.ExecutionContext.global
import concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure, Random, Try}
import scala.concurrent.duration._

object FuturesAndPromises extends App{
  // Futures are functional way of calculating something in parallel.

  def calculateMeaningOfLife(): Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife() // calculates meaning of life on another thread
  } //

  println(aFuture.value)

  println("waiting on the future")
  aFuture.onComplete(t => t match {
    case Success(meaningOfLife) => println(s"The meaning of life is $meaningOfLife")
    case Failure(e) => println(s"I have failed with $e")
  })

  Thread.sleep(3000)

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit =
      println(s"${this.name} poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    //database
    val names = Map(
      "fb.id.asdasd" -> "Tom",
      "fb.id.addas" -> "Mark",
      "fb.id.grewfwe" -> "Greg"
    )

    val friends = Map(
      "fb.id.asdasd" -> "fb.id.addas"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile]= Future {
      // fetching
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFiend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }

  }

  val mark = SocialNetwork.fetchProfile("fb.id.asdasd")
  mark.onComplete( {
    case Success(markProfile) => {
      val bill = SocialNetwork.fetchBestFiend(markProfile)
      bill.onComplete( {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      })
    }
    case Failure(ex) => ex.printStackTrace()

  })

  Thread.sleep(1000)

  val nameOnTheWall = mark.map(profile => profile.name)

  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFiend(profile))

  val tomsBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("T"))

  // for-comprehension
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.asdasd")
    bill <- SocialNetwork.fetchBestFiend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Rock the JVM banking"

    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "Success")
    }

    def purchase(userName: String, item: String, merchantName: String, cost: Double): String = {
      // fetch user from db and create transaction
      // wait for transaction to finsih
      val transactionStatusFuture = for {
        user <- fetchUser(userName)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds)
    }
  }

  println(BankingApp.purchase("Jamie", "iphone 12", "store", 1000))

  // Promises
  val promise = Promise[Int]()
  val future = promise.future

  future.onComplete( {
    case Success(r) => println("[consumer] I've received "  + r)
      Thread.sleep(1000)
      // "fulfilling" the promise
      promise.success(42)
      println("[producer] done")
  })
  Thread.sleep(1000)


  // Fulfil a future immediately with a value
  def fulfillImmediately[T](value: T): Future[T] = Future(value)

  // Insequence
  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] = {
    first.flatMap(_ => second)
  }

  // first out of 2 futures
  /* def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]

    def tryComplete(promise: Promise[A], result: Try[A]): Nothing = result match {
      case Success(r) => try {
        promise.success(r)
      } catch {
        case _ =>
      }
      case Failure(t) => try {
        promise.failure(t)
      } catch {
        case _ =>
      }
    }

    fa.onComplete(tryComplete(promise, _))
    fb.onComplete(tryComplete(promise, _))

    promise.future
  }
  */

  // Last out of 2 futures
  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // 1 promise which both futures will try to complete
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]

    lastPromise.future
  }

  

}


