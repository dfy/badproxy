package badproxy

import scala.concurrent.duration._
import scala.concurrent.Future
import akka.pattern.{ask, after}
import akka.util.Timeout
import akka.actor._
import akka.io.IO
import spray.can.Http
import spray.can.server.Stats
import spray.util._
import spray.http._
import spray.http.HttpHeaders._
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

    case req: HttpRequest =>
      
      /*req.header[Host] match {
        case None                   ⇒ log.info("Request has a relative URI and is missing a `Host` header")
        case Some(Host("", _))      ⇒ log.info("Request has a relative URI and an empty `Host` header")
        case Some(Host(host, port)) ⇒ log.info("Request has host " + host + " and port " + port + " with path " + req.uri)
      }*/

      val newRequest = updateRequestHostAndPort(req, "localhost", 3000)
      //log.info("About to ask IO(Http) to make a request")
      //log.info(newRequest.toString)

      val client = sender
      //IO(Http).ask(newRequest).flatMap(r => Future(r)).mapTo[HttpResponse] onSuccess {
      IO(Http)
        .ask(newRequest)
        .flatMap(r => 
          after(2 seconds, using = system.scheduler)(Future(r))
        )
        .mapTo[HttpResponse] onSuccess {
          case resp: HttpResponse => 
            //log.info("Got the HttpResponse...")
            //log.info(resp.toString)
            client ! resp
          case _ => 
            sys.error("BOOM!")
        }
  }

  def updateRequestHostAndPort(req: HttpRequest, host: String, port: Int): HttpRequest = {
    val newAuth = req.uri.authority.copy(host = new Uri.NamedHost(host), port = port)
    val newUri = req.uri.copy(authority = newAuth)

    val hostWithIndex = req.headers.zipWithIndex.find(_._1.isInstanceOf[Host])
    val newHeaders = hostWithIndex match {
      case Some((_, index: Int)) => 
        req.headers.updated(index, Host(host))
      case _ => 
        req.headers
    }

    req.copy(uri = newUri, headers = newHeaders)
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