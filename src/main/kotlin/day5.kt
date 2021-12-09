import kotlin.math.abs

class Vector(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
) {
    fun isHorizontal(): Boolean {
        return y1 == y2
    }

    fun isVertical(): Boolean {
        return x1 == x2
    }

    fun isDiagonal(): Boolean {
        return abs(x1 - x2) == abs(y1 - y2)
    }

    fun getPoints(): List<Pair<Int, Int>> {
        if (isHorizontal()) {
            return (x1 toward x2).zip(Array(abs(x2 - x1) + 1) { y1 })
        }
        if (isVertical()) {
            return (Array(abs(y2 - y1) + 1) { x1 }).zip(y1 toward y2)
        }
        if (isDiagonal()) {
            return (x1 toward x2).zip(y1 toward y2)
        }

        throw Error("invalid vector")
    }

    override fun toString(): String {
        return "Vec(($x1, $y1), ($x2, $y2)))"
    }
}

fun parseInputLines(): List<Vector> {
    val lines = readInput("day5.txt")

    fun parseVector(v: String): Vector {
        val crds = v.trim().split("-", ">", " ", ",")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
        return Vector(crds[0], crds[1], crds[2], crds[3])
    }

    return lines.map { parseVector(it) }
}

fun solveDay5Part1() {
    val MAX_X = 1000
    val MAX_Y = 1000

    val vectors = parseInputLines()
    val board: Array<Array<Int>> = Array(MAX_Y) { Array(MAX_X) { 0 } }

    for (vec in vectors) {
        if (vec.isHorizontal() || vec.isVertical() || vec.isDiagonal()) {
            for (point in vec.getPoints()) {
                val (x, y) = point
                board[y][x] += 1
            }
        }
    }

    println(board.sumOf { row -> row.sumOf { cell -> if (cell >= 2) 1.toInt() else 0 }})
}