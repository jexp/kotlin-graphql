package demo

/**
 * @author mh
 * @since 12.06.18
 */
fun main(args: Array<String>) {
    println("Hello ${args[0]}")
    demo()
}

fun demo() {
    val data = listOf(mapOf("name" to "Kotlin", "downloads" to 42), mapOf("name" to "graphql-java", "downloads" to 42))

    val result = data.flatMap{ it.values.filterIsInstance<Int>() }.reduce{a,b -> a+b}

    println(result)
}
