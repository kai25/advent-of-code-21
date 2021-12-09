fun solveDay8Part1() {
    val entries = readInput("day8.txt")

    var counter = 0
    val valuableSizes = intArrayOf(2, 3, 4, 7)
    for (entry in entries) {
        val msg = entry.split("|").last().split(" ")
            .filter { it.isNotEmpty() }

        counter += msg.sumOf { if (valuableSizes.indexOf(it.length) > -1) 1.toInt() else 0 }
    }

    println(counter)
}

// a - 8
// b - 6
// c - 8
// d - 7
// e - 4
// f - 9
// g - 7

val DIGITS_ENCODING = mapOf<String, Int>(
    "abcefg" to 0,
    "cf" to 1,
    "acdeg" to 2,
    "acdfg" to 3,
    "bcdf" to 4,
    "abdfg" to 5,
    "abdefg" to 6,
    "acf" to 7,
    "abcdefg" to 8,
    "abcdfg" to 9,
)

fun decodeMsg(msg: String, decodedChars: Map<String, String>): String {
    val x = msg.split("").filter { it.isNotEmpty() }.map { ch -> decodedChars[ch].toString() }.sorted().joinToString("")
    return DIGITS_ENCODING[x].toString()
}

fun decode(values: List<String>): Map<String, String> {
    val occurrences = values.joinToString("").groupingBy { it }.eachCount()
    val fourth = values.first { it.length == 4 }

    fun findMatch(count: Int?): String {
        return occurrences.entries.first { it.value == count }.key.toString()
    }

    fun findMatches(count: Int?): List<String> {
        return occurrences.entries.filter { it.value == count }.map { it.key.toString() }
    }

    val decodedChars: MutableMap<String, String> = mutableMapOf(
        "a" to "",
        "b" to findMatch(6),
        "c" to "",
        "d" to "",
        "e" to findMatch(4),
        "f" to findMatch(9),
        "g" to "",
    )

    // a, c

    val (z1, z2) = findMatches(8)
    if (z1 in fourth) {
        decodedChars["c"] = z1
        decodedChars["a"] = z2
    } else {
        decodedChars["a"] = z1
        decodedChars["c"] = z2
    }

    // d, g
    val (x1, x2) = findMatches(7)
    if (x1 in fourth) {
        decodedChars["d"] = x1
        decodedChars["g"] = x2
    } else {
        decodedChars["g"] = x1
        decodedChars["d"] = x2
    }

    return decodedChars.entries.associateBy({ it.value }) { it.key }
}


fun solveDay8Part2() {
    val entries = readInput("day8.txt")

    var s: Long = 0
    for (entry in entries) {
        val (encodedValues, msg) = entry.split("|").map { it.split(" ").map { s -> s.trim() }.filter { s -> s.isNotEmpty()  }}

        val decodedChars = decode(encodedValues)
        val sortedMsgs = msg.map { it.toList().sorted().joinToString("") }
        val decoded = sortedMsgs.map { decodeMsg(it, decodedChars) }.joinToString("")
        s += decoded.toInt()
    }
    print(s)
}