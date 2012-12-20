package badproxy.controller

import java.net.URI

import org.jboss.netty.handler.codec.http.{ HttpRequest, HttpResponse }

class RecorderController {
	def receiveRequest(request: HttpRequest) {}
	def receiveResponse(request: HttpRequest, response: HttpResponse) {}
	def secureConnection(securedHostURI: URI) {}
}