import java.util.*

//fun calculateOutput(formulas: Map<String, String>, template: String): String {
//    var newState = template.first().toString()
//
//    template.windowed(4).forEach {
//        val innerChar = formulas[it]!!
//        newState += innerChar + it.last()
//    }
//
//    return newState
//}
//
//fun getAllVariants(size: Int): List<String> {
//    val chars = listOf('N', 'C', 'H', 'B')
//
//    if (size == 1) {
//        return chars.map { it.toString() }
//    }
//
//    return getAllVariants(size - 1).map { s -> chars.map { s + it } }.flatten()
//}

fun solveDay14Part1() {
    val inputLines = readInput("day14.txt")
    val template = inputLines.first()
    val formulas = inputLines
        .drop(1)
        .map { s -> s.split("->").filter { it.isNotEmpty() } }
        .associate { (a, b) -> a.trim() to (b.trim() + a.trim().last()) }

    var state: String = template
    for (ix in 0..9) {
        val newState = StringBuffer()
        newState.append(state.first())

        state.windowed(2).forEach {
            val res = formulas[it]!!
            newState.append(res)
        }
        state = newState.toString()
    }

    val counts = state
        .split("")
        .filter { it.isNotEmpty() }
        .groupingBy { it }
        .eachCount()
        .entries
        .map { (s, i) -> Pair(i, s) }
        .sortedBy { it.first }

    println(counts)

    println(counts.last().first - counts.first().first)
}


fun solveDay14Part2() {
    val inputLines = readInput("day14.txt")
    val template = inputLines.first()
    val formulas = inputLines
        .drop(1)
        .map { s -> s.split("->").filter { it.isNotEmpty() } }
        .associate { (a, b) -> a.trim() to b.trim() }

    fun getNewCounter(): MutableMap<String, Long> {
        return formulas.entries.associate { (k, _) -> k to 0.toLong() }.toMutableMap()
    }

    var counters = template.windowed(2).groupingBy { it }.eachCount()
        .entries.associate { (k, count) -> k to count.toLong() }.toMutableMap()

    for (ix in 0..39) {
        val newCounter = getNewCounter()

        for ((seq, count) in counters.entries) {
            val midChar = formulas[seq]!!
            val before = seq.first() + midChar
            newCounter[before] =  newCounter.getOrDefault(before, 0) + count
            val after = midChar + seq.last()
            newCounter[after] =  newCounter.getOrDefault(after, 0) + count
        }
        counters = newCounter
    }

    val charCounts = getNewCounter().keys.joinToString("").associate { it.toChar() to 0.toLong() }.toMutableMap()

    for ((k, v) in counters.entries) {
        charCounts[k.first()] = charCounts.getOrDefault(k.first(), 0.toLong()) + v.toLong()
        charCounts[k.last()] = charCounts.getOrDefault(k.last(), 0.toLong()) + v.toLong()
    }

    println(counters)

    val sortedCounts = charCounts.entries
        .map { (s, i) -> Pair((i / 2.0 + 0.5).toLong(), s) }
        .sortedBy { it.first }

    println(sortedCounts)

    println(sortedCounts.last().first - sortedCounts.first().first)
}
