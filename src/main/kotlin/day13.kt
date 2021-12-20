fun solveDay13Part1(): Set<Pair<Int, Int>> {
    val lines = readInput("day13_test.txt")
    val rawPoints = lines.filter { "," in it }
    var points = rawPoints
        .map { it.split(",") }
        .map { (x, y) -> Pair(x.toInt(), y.toInt()) }
        .toSet()

    val folds = lines
        .filter { "=" in it}
        .map { it.split(" ").last() }
        .map { it.split("=") }
        .map { (a, b) -> Pair(a, b.toInt()) }

    for (fold in folds) {
        val (direction, coordinate) = fold

        if (direction == "y") {
            val foldedPoints = points
                .filter { (_, y) -> y > coordinate }
                .map { (x, y) -> Pair(x, kotlin.math.abs(y - 2 * coordinate)) }
                .toSet()

            points = points.filter { (_, y) -> y < coordinate }.toSet()
            points = points + foldedPoints
        } else {
            val foldedPoints = points
                .filter { (x, _) -> x > coordinate }
                .map { (x, y) -> Pair(kotlin.math.abs(x - 2 * coordinate), y) }
                .toSet()

            points = points.filter { (x, _) -> x < coordinate }.toSet()
            points = points + foldedPoints
        }
    }

    return points
}

fun solveDay13Part2() {
    val points = solveDay13Part1()

    val matrix = Array(100) { Array(100) { " " } }
    for ((x, y) in points) {
        matrix[y][x] = "#"
    }

    for (row in matrix) {
        println(row.joinToString(""))
    }
}