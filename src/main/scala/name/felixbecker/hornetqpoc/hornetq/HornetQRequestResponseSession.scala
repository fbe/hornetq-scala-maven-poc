package name.felixbecker.hornetqpoc.hornetq

import org.hornetq.api.core.client.ClientSession

/**
 * Created by becker on 1/10/14.
 */
case class HornetQRequestResponseSession(session : ClientSession, consumerNumber : Int) {
  session.start
  session.createTemporaryQueue(consumerNumber.toString, consumerNumber.toString)

  val consumer = session.createConsumer(consumerNumber.toString)

  val producer = session.createProducer(consumerNumber.toString)

}
