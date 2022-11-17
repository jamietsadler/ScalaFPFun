package lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App{

  /* Producer-Consumer problem
    producer -> [ x ] (sets value inside container) -> consumer (processes value inside container)
  */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def set(newValue: Int): Unit = value = newValue
    def get: Unit = { // consumer
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[Consumer] waiting...")
      while (container.isEmpty){
        println("[Consumer] actively waiting...")
      }
      println("[consumer] I have consumed + " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produced, after long work, the value " + value)
      container.set(value)

    })

    consumer.start()
    producer.start()
  }

  //naiveProdCons()

  // wait and notify
  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[container] waiting..")
      container.synchronized {
        container.wait() // will unlock thread until producer signals to give go ahead.
      }
      // container must have some value
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] hard at work...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized( {
        println("[producer] Im producing " + value)
        container.set(value)
        container.notify()
      })
    })

    consumer.start()
    producer.start()
  }

  //smartProdCons()

  // producer -> [ ? ? ? ] -> consumer
  // producer may keep producing values
  // producer and consumer may block each other

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3 // cant produce/consume more than 3 values without blocking

    val consumer = new Thread(() => {
      val random = new Random()
      while(true){
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
            buffer.notify()
          }
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)
        }

        Thread.sleep(random.nextInt(500))
      }

    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity){
            println("[producer]  buffer is full, waiting...")
            buffer.wait()
          }
          // there must be at least one empty space in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)
          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }

    })

    consumer.start()
    producer.start()
  }

  //prodConsLargeBuffer()

  // Multiple producers and consumers acting on same buffer

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit ={
      val random = new Random()
      while (true) {
        buffer.synchronized {
          // producer produces value, 2 consumers are waiting
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting...")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"[consumer $id] consumed " + x)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(250))
    }

   }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer]  buffer is full, waiting...")
            buffer.wait()
          }
          // there must be at least one empty space in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)
          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }

    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20

    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
  }


  //multiProdCons(3, 6)

  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting...")
        bell.wait()
        println(s"[thread $i] hooray")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(3000)
      println("[announcer] rock n roll")
      bell.synchronized {
        bell.notify()
      }
    }).start()
  }

  //testNotifyAll()

  case class Friend(name: String) {
    def bow(other: Friend): Unit = {
      this.synchronized {
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: My friend has risen")
      }
    }

    def rise(other: Friend): Unit = {
      this.synchronized {
        println(s"$this: I am rising to my friend $other")
      }
    }

    var side = "right"

    def switchSide(): Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {
      while (this.side == other.side) {
        println(s"$this: Oh, but please $other feel free to pass")
        switchSide()
        Thread.sleep(500)
      }

    }
  }



  val sam = Friend("Sam")
  val pierre = Friend("Pierre")

  new Thread(() => sam.pass(pierre)).start() // sams block, then pierre's block
  new Thread(() => pierre.pass(sam)).start() // pierres block, then sams block
  // no thread can progress because theyve locked each other out
  // threads have locked object in reverse order.


}
