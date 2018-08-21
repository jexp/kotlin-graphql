package graphql

import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry

class GraphQLDemo {
        fun schema(schema : String) : GraphQLSchema {
	        val schemaParser = SchemaParser()
	        val typeDefinitionRegistry = schemaParser.parse(schema)
	
	        val runtimeWiring = RuntimeWiring.newRuntimeWiring()
	                .type("Query", {builder -> builder.dataFetcher("hello", StaticDataFetcher("world"))})
	                .build()
	
	        val schemaGenerator = SchemaGenerator()
	        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
        }
    fun run() : ExecutionResult {
        val idl  = "type Query {hello: String}"
        val instance = GraphQL.newGraphQL(schema(idl)).build()
        return instance.execute("{hello}")

    }
}

fun main(args: Array<String>) {
    val result = GraphQLDemo().run()
    println(result.getData() as Any)
}
