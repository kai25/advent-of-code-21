fun getPathLen(matrix: List<List<Int>>, path: List<Pair<Int, Int>>): Int {
    return path.sumOf { (x, y) -> matrix[y][x] }
}

fun getShortestPath(matrix: List<List<Int>>, paths: List<List<Pair<Int, Int>>>): List<Pair<Int, Int>>? {
    return paths.minByOrNull { getPathLen(matrix, it) }
}

fun getMinCellAround(matrix: List<List<Int>>, x: Int, y: Int): Int {
    val cells = mutableListOf<Triple<Int, Int, Int>>()
    if (x - 1 in matrix.first().indices) {
        cells.add(Triple(matrix[y][x - 1], x - 1, y))
    }
    if (x + 1 in matrix.first().indices) {
        cells.add(Triple(matrix[y][x + 1 ], x + 1, y))
    }
    if (y - 1 in matrix.indices) {
        cells.add(Triple(matrix[y - 1][x], x, y - 1))
    }
    if (y + 1 in matrix.indices) {
        cells.add(Triple(matrix[y + 1][x], x, y + 1))
    }

    return cells.sortedBy { it.first }.first().first
}


//fun findPath(
//    matrix: List<List<Int>>,
//    distanceMatrix: MutableList<MutableList<Int>>,
//    visited: MutableList<MutableList<Boolean>>,
//    x: Int,
//    y: Int,
//) {
//    if (x == matrix.last().count() - 1 && y == matrix.last().count()) {
//        return
//    }
//
//    val curDistance = distanceMatrix[y][x] +
//
//    if (x + 1 in matrix.first().indices && !visited[y][x]) {
//        visited[y][x] = true
//        val nextDistance = distanceMatrix[y][x]
//    }
//    if (y + 1 in matrix.indices && Pair(x, y + 1) !in currentPath) {
//
//    }
//
//    return getShortestPath(matrix, currentPaths)
//}



fun solveDay15Part1() {
    val inputLines = readInput("day15.txt")
    val matrix = inputLines
        .map { it.split("").filter { s -> s.isNotEmpty() }.map { s -> s.toInt() } }

    val distanceMatrix = List(matrix.size) { MutableList(matrix.size) { Int.MAX_VALUE / 2 } }
    dpath(matrix, distanceMatrix)

    println(distanceMatrix.last().last())
}


fun solveDay15Part2() {
    val inputLines = readInput("day15.txt")
    val tileMatrix = inputLines
        .map { it.split("").filter { s -> s.isNotEmpty() }.map { s -> s.toInt() } }
    val startSize = tileMatrix.size
    val matrix = MutableList(tileMatrix.size * 5) { MutableList(tileMatrix.size * 5) { 0 } }

    for (y in tileMatrix.indices) {
        for (x in tileMatrix[y].indices) {
            for (offsetX in 0 until 5) {
                for (offsetY in 0 until 5) {
                    val cellVal = tileMatrix[y][x] + offsetX + offsetY
                    matrix[y + offsetY * startSize][x + offsetX * startSize] = if (cellVal < 10) cellVal else (cellVal % 10) + 1
                }
            }
        }
    }

    val distanceMatrix = List(matrix.size) { MutableList(matrix.size) { Int.MAX_VALUE / 2 } }
    for (ix in 0..10) {
        println("try #$ix")
        dpath(matrix, distanceMatrix)
        println(distanceMatrix.last().last())
    }
}

fun printMatrix(matrix: List<List<Int>>) {
    for (row in matrix) {
        println(row.joinToString("|"))
    }
    println("")
}

fun processCell(
    matrix: List<List<Int>>,
    distanceMatrix: List<MutableList<Int>>,
    x: Int,
    y: Int,
) {
    val min = getMinCellAround(distanceMatrix, x, y)
    val newVal = min + matrix[y][x]
    if (distanceMatrix[y][x] > newVal) {
        distanceMatrix[y][x] = newVal
    }
}


fun dpath(
    matrix: List<List<Int>>,
    distanceMatrix: List<MutableList<Int>>,
) {
    distanceMatrix[0][0] = 0

    for (y in matrix.indices) {
        for (x in matrix.first().indices) {
            processCell(matrix, distanceMatrix, x, y)
        }
    }

    for (x in matrix.first().indices) {
        for (y in matrix.indices) {
            processCell(matrix, distanceMatrix, x, y)
        }
    }


    for (y in matrix.indices.reversed()) {
        for (x in matrix.first().indices) {
            processCell(matrix, distanceMatrix, x, y)
        }
    }

    for (x in matrix.first().indices) {
        for (y in matrix.indices.reversed()) {
            processCell(matrix, distanceMatrix, x, y)
        }
    }

    for (y in matrix.indices.reversed()) {
        for (x in matrix.first().indices.reversed()) {
            processCell(matrix, distanceMatrix, x, y)
        }
    }
}