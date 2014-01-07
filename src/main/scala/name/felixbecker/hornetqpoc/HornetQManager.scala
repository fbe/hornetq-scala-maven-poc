package name.felixbecker.hornetqpoc

import org.hornetq.api.core.client.{ClientSessionFactory, HornetQClient}
import org.hornetq.api.core.TransportConfiguration
import org.hornetq.core.config.impl.FileConfiguration
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory
import org.hornetq.core.server.embedded.EmbeddedHornetQ

object HornetQManager {

  private val serverLocator = HornetQClient.createServerLocatorWithHA(new TransportConfiguration(classOf[InVMConnectorFactory].getName))

  val hornetQ = new EmbeddedHornetQ()
  val configuration = new FileConfiguration("hornetq-embedded-configuration.xml")

  //val sessionFactory = serverLocator.createSessionFactory
  hornetQ.setConfiguration(configuration)

  private var sfOption : Option[ClientSessionFactory] = None

  def sessionFactory = {
    sfOption.getOrElse {
      throw new IllegalStateException("tried to get session factory but hornetq isn't started yet")
    }
  }


  @volatile
  private var started = false


  def start = synchronized {
    if (!started) {
      started = true
      configuration.start
      hornetQ.start
      sfOption = Some(serverLocator.createSessionFactory)
    } else {
      throw new IllegalStateException("HornetQ already started")
    }
  }

  def stop = synchronized {
    if (started) {
      started = false
      sfOption.get.close
      sfOption = None
      hornetQ.stop
      configuration.stop
    } else {
      throw new IllegalStateException("HornetQ already stopped")
    }
  }
}
