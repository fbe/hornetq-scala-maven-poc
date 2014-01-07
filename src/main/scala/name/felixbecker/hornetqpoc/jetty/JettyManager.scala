package name.felixbecker.hornetqpoc.jetty

import org.eclipse.jetty.server.Server

object JettyManager {

  def startJetty() {
    val jetty = new Server(8080)
    jetty.setHandler(SampleHandler)
    jetty.start
    jetty.join
  }
}
