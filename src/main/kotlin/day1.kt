fun gt(x: Int, y: Int): Int = if (x > y) 1 else 0

fun solveDay1Part1() {
    val lines = readInput("day1.txt")
    val depthValues: List<Int> = lines.map { it.toInt() }
    val result = depthValues.windowed(2).sumOf { (a, b) -> gt(b, a) }
    println("Result = $result")
}

fun solveDay1Part2() {
    val lines = readInput("day1.txt")
    val depthValues: List<Int> = lines.map { it.toInt() }
    val result = depthValues.windowed(3).map { it.sum() }.windowed(2).sumOf { (a, b) -> gt(b, a) }
    println("Result = $result")
}
