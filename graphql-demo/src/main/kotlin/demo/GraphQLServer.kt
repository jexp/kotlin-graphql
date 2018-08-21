package demo

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetcher
import graphql.schema.StaticDataFetcher
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

val MAPPER = ObjectMapper()

data class Request(val query:String, val variables:Any? = null, val operationName : String? = null) {
    // for GraphiQL
    val params get() =
            when (variables) {
                is String -> MAPPER.readValue(variables, Map::class.java)
                is Map<*, *> -> variables
                else -> emptyMap<String, Any>()
            } as Map<String, Any>
}

fun main(args: Array<String>) {

    val schema = """type Query {
                           answer: Int
                           hello(what:String="World"): String
                        }"""

    val fetchers = mapOf("Query" to
            listOf("hello" to DataFetcher { env -> "Hello "+env.getArgument("what") },
                    "answer" to StaticDataFetcher(42)))

    val handler = GraphQLHandler(schema, fetchers)

    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { jackson { } }
        routing {
            post("/graphql") {
                val request = call.receive<Request>()
                val result = handler.execute(request.query, request.params)
                call.respond(mapOf("data" to result.getData<Any>()))
            }
        }
    }
    server.start(wait = true)
    //  curl localhost:8080/graphql -d'{"query":"{answer}"}' -H content-type:application/json
    //  {"data":{"answer":42}}
}
