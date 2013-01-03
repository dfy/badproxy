/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package badproxy.http.handler

import java.net.{ URI, InetSocketAddress }

import org.jboss.netty.channel.{ ChannelHandlerContext, ChannelFuture }
import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.handler.codec.http.HttpHeaders._ 

import badproxy.config.ProxyConfig
import badproxy.controller.RecorderController
import badproxy.http.channel.BootstrapFactory.newClientBootstrap

class BrowserHttpRequestHandler(controller: RecorderController, proxyConfig: ProxyConfig) extends AbstractBrowserRequestHandler(controller, proxyConfig) {

	def connectToServerOnBrowserRequestReceived(ctx: ChannelHandlerContext, request: HttpRequest): ChannelFuture = {

		val bootstrap = newClientBootstrap(controller, ctx, request, false)

		val hostMap = Map(
			"localhost:8080" -> ("localhost", 80)
		)

		val (proxyHost, proxyPort) = (for {
			host <- proxyConfig.host
			port <- proxyConfig.port
		} yield (host, port))
			.getOrElse {
				hostMap.getOrElse(
					getHost(request, "unknown"), 
					("unknown", 80)
				)
			}

		bootstrap.connect(new InetSocketAddress(proxyHost, proxyPort))
	}
}
