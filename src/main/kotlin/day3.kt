fun parseBits(): List<List<Int>> {
    return readInput("day3.txt")
        .map { it.map { ch -> ch.toString().toInt() } }
}

fun solveDay3Part1() {
    val messages = parseBits()
    val msgLen = messages.first().size
    val totalLen = messages.size

    val mostCommon: ArrayList<Char> = ArrayList()
    val mostRare: ArrayList<Char> = ArrayList()

    for (ix in 0 until msgLen) {
        val sum = messages.sumOf { it[ix] }.toFloat()

        if (totalLen / sum >= 2) {
            mostCommon.add('0')
            mostRare.add('1')
        } else {
            mostCommon.add('1')
            mostRare.add('0')
        }
    }

    val gammaRate = mostCommon.joinToString("").toInt(2)
    val epsilonRate = mostRare.joinToString("").toInt(2)

    println("G = $gammaRate, E = $epsilonRate, G * E = ${gammaRate * epsilonRate}")
}

fun solveDay3Part2() {
    val messages = parseBits()
    val msgLen = messages.first().size

    var mostCommon = messages
    for (ix in 0 until msgLen) {
        val sum = mostCommon.sumOf { it[ix] }.toFloat()
        val mostCommitBit = if (sum * 2 >= mostCommon.size) 1 else 0
        mostCommon = mostCommon.filter { it[ix] == mostCommitBit }

        if (mostCommon.size == 1)
            break
    }

    var mostRare = messages
    for (ix in 0 until msgLen) {
        val sum = mostRare.sumOf { it[ix] }.toFloat()
        val mostRareBit = if (sum * 2 < mostRare.size) 1 else 0
        mostRare = mostRare.filter { it[ix] == mostRareBit }

        if (mostRare.size == 1)
            break
    }

    val a = mostCommon.first().joinToString("").toInt(2)
    val b = mostRare.first().joinToString("").toInt(2)
    println("a = $a, b = $b, a * b = ${a * b}")
}