package graphql

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        install(CallLogging)
        install(ContentNegotiation) {
            jackson {
            }
        }
        routing {
            get("/") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }
            get("/demo") {
                call.respondText("HELLO WORLD!")
            }
            get("/json") {
                call.respond(mapOf("OK" to true))
            }
            post("/graphql") {

            }
        }
    }
    server.start(wait = true)
}
