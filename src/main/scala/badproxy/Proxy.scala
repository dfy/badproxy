package badproxy

import badproxy.controller.RecorderController
import badproxy.config.ProxyConfig
import badproxy.http.GatlingHttpProxy

import grizzled.slf4j.Logging

object Proxy extends Logging {
	def main(args: Array[String]) {
		info("Start the bad proxy!")

		val config = new ProxyConfig
		val controller = new RecorderController
		val proxy = new GatlingHttpProxy(
			controller, 
			8080, 
			4343, 
			config
		)
		
		info("End of the bad proxy!")
	}
}