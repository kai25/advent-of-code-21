fun parseCommands(): List<Pair<String, Int>> {
    val inputLines = readInput("day2.txt")
    return inputLines
        .map { it.split(' ') }
        .map { (a, b) -> Pair(a, b.toInt()) }
}

fun solveDay2Part1() {
    var x = 0
    var y = 0

    for ((direction, len) in parseCommands()) {
        when (direction) {
            "forward" -> x += len
            "down" -> y += len
            "up" -> y -= len
        }
    }

    println("result = ${x * y}")
}

fun solveDay2Part2() {
    var x = 0
    var y = 0
    var aim = 0

    for ((direction, len) in parseCommands()) {
        when (direction) {
            "forward" -> {
                x += len
                y += aim * len
            }
            "down" -> aim += len
            "up" -> aim -= len
        }
    }

    println("result = ${x * y}")
}
