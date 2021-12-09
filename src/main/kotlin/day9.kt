fun findMinimums(matrix: List<List<Int>>): List<Pair<Int, Int>> {
    val maxY = matrix.size
    val maxX = matrix.first().size
    val maxVal = 10

    val minimums: MutableList<Pair<Int, Int>> = mutableListOf()

    for (y in matrix.indices) {
        for (x in matrix[y].indices) {
            val center = matrix[y][x]

            val up = if (y == 0) maxVal else matrix[y - 1][x]
            if (center >= up) {
                continue
            }
            val left = if (x == 0) maxVal else matrix[y][x - 1]
            if (center >= left) {
                continue
            }
            val down = if (y == maxY - 1) maxVal else matrix[y + 1][x]
            if (center >= down) {
                continue
            }
            val right = if (x == maxX - 1) maxVal else matrix[y][x + 1]
            if (center >= right) {
                continue
            }

            minimums.add(Pair(x, y))
        }
    }

    return minimums
}

fun solveDay9Part1() {
    val lines = readInput("day9_test.txt")

    val matrix: List<List<Int>> = lines
        .map { it.split("").filter { x -> x.isNotEmpty() }.map { x -> x.toInt() } }

    val minimums = findMinimums(matrix)
    println(minimums.count())
}

fun findBasin(startX: Int, startY: Int, matrix: MutableList<MutableList<Int>>): Int {
    val curValue = matrix[startY][startX]
    if (curValue == 9) {
        return 0
    }

    matrix[startY][startX] = -1

    var counter = 1

    if (startX - 1 in matrix.first().indices && curValue < matrix[startY][startX - 1]) {
        counter += findBasin(startX - 1, startY, matrix)
    }
    if (startY - 1 in matrix.indices && curValue < matrix[startY - 1][startX]) {
        counter += findBasin(startX, startY - 1, matrix)
    }
    if (startX + 1 in matrix.first().indices && curValue < matrix[startY][startX + 1]) {
        counter += findBasin(startX + 1, startY, matrix)
    }
    if (startY + 1 in matrix.indices && curValue < matrix[startY + 1][startX]) {
        counter += findBasin(startX, startY + 1, matrix)
    }

    return counter
}

fun solveDay9Part2() {
    val lines = readInput("day9_test.txt")

    val matrix: MutableList<MutableList<Int>> = lines
        .map { it.split("").filter { x -> x.isNotEmpty() }.map { x -> x.toInt() }.toMutableList() }
        .toMutableList()

    val minimums = findMinimums(matrix)

    val sizes = mutableListOf<Int>()
    for ((mX, mY) in minimums) {
        sizes.add(findBasin(mX, mY, matrix.map { it.map { x -> x }.toMutableList() }.toMutableList()))
    }

    val final = sizes.sorted().takeLast(3)
    println(final[0] * final[1] * final[2])
}