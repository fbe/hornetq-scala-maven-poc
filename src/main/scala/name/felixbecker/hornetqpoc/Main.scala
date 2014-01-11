package name.felixbecker.hornetqpoc
import org.hornetq.api.core.HornetQQueueExistsException
import name.felixbecker.hornetqpoc.jetty.JettyManager
import name.felixbecker.hornetqpoc.messages.CustomerInterestedInArticleDetailsMessageResponse
import play.api.libs.json.Json

object Main extends App {

  implicit val msgFormat = Json.format[CustomerInterestedInArticleDetailsMessageResponse]

  println(msgFormat.writes(CustomerInterestedInArticleDetailsMessageResponse(Some("x"))))
  println(msgFormat.writes(CustomerInterestedInArticleDetailsMessageResponse(None)))

  HornetQManager.start
  JettyManager.startJetty

}
