package demo

import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.DataFetcher
import graphql.schema.GraphQLSchema
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.runBlocking

class GraphQLHandler(private val schema:GraphQLSchema) {

    constructor(schema:String, fetchers: Map<String, List<Pair<String, DataFetcher<*>>>>) : this(schema(schema,fetchers))

    companion object {
    fun schema(schema: String, fetchers: Map<String,List<Pair<String, DataFetcher<*>>>> = emptyMap()): GraphQLSchema {

        val typeDefinitionRegistry = SchemaParser().parse(schema)

        val runtimeWiring = newRuntimeWiring()
            .apply { fetchers.forEach { (type, fields) -> this.type(type)
            { builder -> fields.forEach { (field, fetcher) -> builder.dataFetcher(field, fetcher) };builder } } }
            .build()

        return SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
    }
    }
    suspend
    fun execute(query: String, params: Map<String, Any> = emptyMap(), op:String? = null, ctx : Any? = null): ExecutionResult {
        val graphql = GraphQL.newGraphQL(schema).build()
        val executionResult = graphql.executeAsync{
            builder -> builder.query(query).variables(params).operationName(op).context(ctx) }

        return executionResult.await()
    }

}

fun main(args: Array<String>) {
    runBlocking {
        val schema = """type Query {
                           answer: Int
                           hello(what:String="World"): String
                        }"""
        val fetchers = mapOf("Query" to
                listOf("hello" to DataFetcher { env -> "Hello "+env.getArgument("what") },
                       "answer" to StaticDataFetcher(42)))

        with(GraphQLHandler(schema, fetchers)) {
            val result = execute(args.first(), args.drop(1).zipWithNext().toMap())
            println(result.getData() as Any)
        }
    }
}
