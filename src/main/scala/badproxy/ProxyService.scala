package badproxy

import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import akka.actor._
import akka.io.IO
import spray.can.Http
import spray.can.server.Stats
import spray.util._
import spray.http._
import HttpMethods._
import MediaTypes._


class ProxyService extends Actor with SprayActorLogging {
  implicit val system = context.system
  implicit val timeout: Timeout = 1.second // for the actor 'asks'
  import context.dispatcher // ExecutionContext for the futures and scheduler

  def receive = {
    // when a new connection comes in we register ourselves as the connection handler
    case _: Http.Connected => 
      sender ! Http.Register(self)

    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      sender ! index

    case HttpRequest(GET, Uri.Path("/proxied"), _, _, _) =>
      val client = sender
      //log.info(header[Host])

      IO(Http).ask(HttpRequest(GET, Uri("http://localhost/~sduffey/"))).mapTo[HttpResponse] onSuccess {
        case resp: HttpResponse => 
          //log.info(resp.toString)
          client ! resp
        case _ => 
          sys.error("BOOM!")
      }
  }

  ////////////// helpers //////////////

  lazy val index = HttpResponse(
    entity = HttpEntity(`text/html`,
      <html>
        <body>
          <h1>Say hello to <i>spray-can</i>!</h1>
        </body>
      </html>.toString()
    )
  )
}