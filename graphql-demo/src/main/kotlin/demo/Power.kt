package demo

import kotlin.collections.Map

val Int.odd : Boolean get() = this % 2 == 1

inline fun <R,reified T> Map<*,*>.aggregate(acc: R, operation: (R,T)->R) =
        values.filterIsInstance<T>().fold(acc, operation)

data class Word(val text:String, val len:Int = text.length)

fun main(args: Array<String>) {
    val message = """Hello ${args.firstOrNull() ?: "GraphQL Europe"}
                    |in ${args.getOrNull(1) ?:"Berlin"} <3""".trimMargin()
    println(message)
    // Hello GraphQL Europe in Berlin <3

    val words = message.split("\\s+".toRegex())
            .withIndex()
            .associate { (i,w) -> i to if (i.odd) w else Word(w)}
            .toSortedMap()

    println(words)
    // {0=Word(text=Hello, len=5), 1=GraphQL, 2=Word(text=Europe, len=6), 3=in, 4=Word(text=Berlin, len=6), 5=<3}

    println(words.aggregate(1) { a, w:Word -> a*w.len})
    // 180
}

