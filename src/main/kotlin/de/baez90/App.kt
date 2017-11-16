package de.baez90

import com.orbitz.consul.Consul
import com.orbitz.consul.model.agent.ImmutableRegistration
import com.orbitz.consul.model.agent.Registration
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.net.InetAddress
import java.net.URL

/**
 * @author Peter Kurfer
 * Created on 11/16/17.
 */

private val defaultPort = 8080

fun main(args: Array<String>) {
	var port = getEnv("APP_PORT", "$defaultPort").toIntOrNull()
	port = if (port == null) defaultPort else port
	val hostName = InetAddress.getLocalHost().hostName
	val consulClient = Consul.builder().withUrl(URL("http://${getEnv("CONSUL_URL", "localhost:8500")}")).build()

	val registration = ImmutableRegistration.builder()
			.address(hostName)
			.addAllTags(getFabioTags())
			.name("WhoAmI")
			.port(port)
			.id(hostName)
			.check(Registration.RegCheck.http("http://$hostName:$port/health", 5, 1))
			.build()

	consulClient.agentClient().register(registration)
	val server = embeddedServer(Netty, port) {
		routing {
			get("/") {
				call.respondText("Hello from $hostName", ContentType.Text.Plain)
			}

			get("/health") {
				call.respondText("All fine")
			}
		}
	}
	server.start(wait = true)
}

private fun getEnv(varName: String, fallback: String): String {
	val env = System.getenv()
	if (env.containsKey(varName)) {
		return env[varName]!!
	}
	return fallback
}

private fun getFabioTags() : List<String> {
	val tagEnv = getEnv("FABIO_TAGS", "")
	return tagEnv.split('|').toList()
}