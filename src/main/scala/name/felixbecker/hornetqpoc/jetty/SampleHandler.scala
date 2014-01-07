package name.felixbecker.hornetqpoc.jetty

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.Request
import name.felixbecker.hornetqpoc.HornetQManager
import java.util.UUID

object SampleHandler extends AbstractHandler {

  def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {

    val session = HornetQManager.sessionFactory.createSession(true,true)

    val tempQueueName = UUID.randomUUID.toString

    session.start

    session.createTemporaryQueue(tempQueueName, tempQueueName)

    val consumer = session.createConsumer(tempQueueName)

    val producer = session.createProducer(tempQueueName)

    val message = session.createMessage(false)
    message.putStringProperty("CorrelationId", "1234")

    producer.send(tempQueueName,message)

    producer.close

    val receivedMessage = consumer.receive

    println("received message: " + receivedMessage.getStringProperty("CorrelationId"))

    try {
      response.setContentType("text/html;charset=utf-8")
      response.setStatus(HttpServletResponse.SC_OK)
      baseRequest.setHandled(true)
      response.getWriter().println("oh hai")
    } finally {
      consumer.close
      session.deleteQueue(tempQueueName)

      session.stop
      session.close
    }

  }
}
