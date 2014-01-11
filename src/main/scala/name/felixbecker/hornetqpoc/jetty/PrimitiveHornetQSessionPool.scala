package name.felixbecker.hornetqpoc.jetty

import java.util.concurrent.ArrayBlockingQueue
import org.hornetq.api.core.client.ClientSession
import name.felixbecker.hornetqpoc.HornetQManager
import scala.collection.JavaConversions._
import name.felixbecker.hornetqpoc.hornetq.HornetQRequestResponseSession


object PrimitiveHornetQSessionPool {

    private val sessionQueueSize = 150

    private val sessionQueue = new ArrayBlockingQueue[HornetQRequestResponseSession](sessionQueueSize)

    (1 to sessionQueueSize).foreach(x => {
      sessionQueue.put(new HornetQRequestResponseSession(HornetQManager.sessionFactory.createSession(true,true), x))
    })

    def borrow = sessionQueue.take
    def release(session : HornetQRequestResponseSession) = sessionQueue.put(session)

}
