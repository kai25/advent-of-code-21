val MATCHING_CHARS = mapOf(
    "(" to ")",
    "<" to ">",
    "[" to "]",
    "{" to "}",
)

val POINTS = mapOf(
    ")" to 3,
    "]" to 57,
    "}" to 1197,
    ">" to 25137
)


val COMPLETION_POINTS = mapOf(
    ")" to 1,
    "]" to 2,
    "}" to 3,
    ">" to 4
)


fun findIncompleteLines(lines: List<String>): List<String> {
    val result = mutableListOf<String>()

    for (line in lines) {
        val stack = ArrayDeque<String>()
        var isBroken = false
        for (char in line.split("").filter { it.isNotEmpty() }) {
            if (char in MATCHING_CHARS.keys) {
                stack.addLast(char)
            } else {
                val matching = MATCHING_CHARS[stack.removeLast()]
                if (matching != char) {
                    isBroken = true
                    break
                }
            }
        }

        if (!isBroken) {
            result.add(line)
        }
    }

    return result
}

fun solveDay10Part1() {
    val inputLines = readInput("day10.txt")
    var result = 0

    for (line in inputLines) {
        val stack = ArrayDeque<String>()
        for (char in line.split("").filter { it.isNotEmpty() }) {
            if (char in MATCHING_CHARS.keys) {
                stack.addLast(char)
            } else {
                val matching = MATCHING_CHARS[stack.removeLast()]
                if (matching != char) {
                    result += POINTS[char]!!
                    break
                }
            }
        }
    }
    println("P1=$result")
}

fun solveDay10Part2() {
    val inputLines = readInput("day10.txt")

    val scores = mutableListOf<Long>()
    val lines = findIncompleteLines(inputLines)
    for (line in lines) {
        val stack = ArrayDeque<String>()
        for (char in line.split("").filter { it.isNotEmpty() }) {
            if (char in MATCHING_CHARS.keys) {
                stack.addLast(char)
            } else {
                stack.removeLast()
            }
        }

        var lineScore: Long = 0
        while (stack.isNotEmpty()) {
            val lastChar = stack.removeLast()
            lineScore = lineScore * 5 + COMPLETION_POINTS[MATCHING_CHARS[lastChar]]!!
        }

        scores.add(lineScore)
    }

    println(scores.sorted()[scores.size / 2])
}