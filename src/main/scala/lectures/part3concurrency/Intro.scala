package lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {

  /* Interface runnable{
      public void run
    }
  */

  // JVM Threads
  val aThread = new Thread( new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  aThread.start() // gives signal to start JVM thread
  // JVM thread on top of an OS thread

  aThread.join() // blocks until a thread finishes running

  val threadHello = Thread(() => (1 to 5).foreach(_ => println("Hello")))
  val threadGoodBye = Thread(() => (1 to 5).foreach(_ => println("Goodbye")))

  threadHello.start()
  threadGoodBye.start()

  // different runs in different environments produce different results.

  // executors
  // threads are expensive to start and kill, so reuse
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("Something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("Done after 1 second")
  })
  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("Done after 2 seconds")
  })

  pool.shutdown()
  pool.execute(() => println("Should not appear"))


}
