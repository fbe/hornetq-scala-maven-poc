package name.felixbecker.hornetqpoc

import org.hornetq.api.core.HornetQQueueExistsException
import name.felixbecker.hornetqpoc.jetty.JettyManager

object Main extends App {

  HornetQManager.start
  JettyManager.startJetty

  val session = HornetQManager.sessionFactory.createSession(true, true)

  session.start

  def tryCreateQueue(f: () => Unit) {

    try f() catch {
      case e: HornetQQueueExistsException => println("Warning, didn't create queue " + e.getMessage)
    }

  }

  session.stop

  session.start

  tryCreateQueue(() => {
    session.createQueue("foobarAddress", "mySampleQueue", true)
  })
  tryCreateQueue(() => {
    session.createQueue("foobarAddress", "mySecondSampleQueue", true)
  })


  val producer = session.createProducer("foobarAddress")

  val msg = session.createMessage(false)
  msg.putStringProperty("key", "value")

  producer.send("foobarAddress", msg)

  Thread.sleep(2000)


  val consumer = session.createConsumer("mySampleQueue")
  val consumer2 = session.createConsumer("mySecondSampleQueue")


  val rmsg1 = consumer.receive(1000)
  val rmsg2 = consumer2.receive(1000)

  println("rmsg1: " + rmsg1 + " rmsg2: " + rmsg2)


}
