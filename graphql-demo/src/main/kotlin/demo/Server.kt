package demo

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.title

data class Text(val text:String)

fun main(args: Array<String>) {

    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { jackson { } }
        routing {
            get("/") {
                call.respondText("Hello World!\n", ContentType.Text.Plain)
            }
            get("/json") {
                call.respond(mapOf("OK" to true))
            }
            post("/post/{name}") {
                val word = call.receive<Text>()
                call.respond(Text(word.text+" from "+call.parameters["name"]))
            }
            get("/html") {
                call.respondHtml {
                    head { title { +"Title"} }
                    body { p { +"Body" }}
                }
            }
        }
    }
    server.start(wait = true)
}
