package demo

import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser

fun main(args: Array<String>) {
    val schema = """type Query {
                        hello(what:String = "World"): String
                    }"""

    val runtimeWiring = newRuntimeWiring()
            .type("Query")
                { it.dataFetcher("hello") { env -> "Hello ${env.getArgument<Any>("what")}!" } }
            .build()

    val graphQLSchema = SchemaGenerator().makeExecutableSchema(SchemaParser().parse(schema), runtimeWiring)

    val build = GraphQL.newGraphQL(graphQLSchema).build()
    val executionResult = build.execute("""{ hello (what:"Kotlin") } """)

    println(executionResult.getData<Any>())
    // Prints: {hello=Hello Kotlin!}
}

