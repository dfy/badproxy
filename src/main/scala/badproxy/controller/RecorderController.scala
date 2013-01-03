package badproxy.controller

import java.net.URI

import org.jboss.netty.handler.codec.http.{ HttpRequest, HttpResponse }

import grizzled.slf4j.Logging

class RecorderController extends Logging {

	def receiveRequest(request: HttpRequest) {
		info("Request received")
	}

	def receiveResponse(request: HttpRequest, response: HttpResponse) {
		info("Response received")
	}

	def secureConnection(securedHostURI: URI) {
		info("Secure connection")
	}
}