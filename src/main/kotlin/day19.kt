import java.util.*
import kotlin.collections.HashMap
import kotlin.math.*

fun degToRad(x: Double): Double {
    return x * PI / 180.0
}

class Point(val x: Int, val y: Int, val z: Int) {
    override fun toString(): String {
        return "Point(x=${x}, y=${y}, z=${z})"
    }

    fun rotX(xRotDeg: Int): Point {
        val xRotRad = degToRad(xRotDeg.toDouble())
        val x1 = 1 * x
        val y1 = cos(xRotRad) * y - sin(xRotRad) * z
        val z1 = sin(xRotRad) * y + cos(xRotRad) * z

        return Point(x1, y1.roundToInt(), z1.roundToInt())
    }

    fun rotY(yRotDeg: Int): Point {
        val yRotRad = degToRad(yRotDeg.toDouble())
        val x1 = cos(yRotRad) * x + sin(yRotRad) * z
        val y1 = y
        val z1 = -sin(yRotRad) * x + cos(yRotRad) * z

        return Point(x1.roundToInt(), y1, z1.roundToInt())
    }

    fun rotZ(zRotDeg: Int): Point {
        val zRotRad = degToRad(zRotDeg.toDouble())
        val x1 = cos(zRotRad) * x - sin(zRotRad) * y
        val y1 = sin(zRotRad) * x + cos(zRotRad) * y
        val z1 = z

        return Point(x1.roundToInt(), y1.roundToInt(), z1)
    }

    fun plus(another: Point): Point {
        return Point(x + another.x, y + another.y, z + another.z)
    }

    fun manhattanDistance(another: Point): Int {
        return abs(x - another.x) + abs(y - another.y) + abs(z - another.z)
    }

    fun hashVector(another: Point): Int {
        return Triple(x - another.x, y - another.y, z - another.z).hashCode()
    }

    override fun hashCode(): Int {
        return this.toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other.toString() == this.toString()
    }
}



fun getVectors(points: List<Point>): Map<Int, Pair<Point, Point>> {
    val result = HashMap<Int, Pair<Point, Point>>()

    points.forEach {
        point ->
        run {
            points.forEach { anotherPoint ->
                run {
                    if (point != anotherPoint) {
                        result[point.hashVector(anotherPoint)] = Pair(point, anotherPoint)
                    }
                }
            }
        }
    }

    return result
}

class BScanner(val id: Int, var points: List<Point>, var position: Point = Point(0, 0, 0))


fun solveDay19() {
    val lines = readInput("day19.txt")

    var curScanner = -1
    val scannerPoints = MutableList(100) { mutableListOf<Point>() }

    for (line in lines) {
        if ("scanner" in line) {
            curScanner += 1
            continue
        }
        val parsedPoint = line.split(",").map { it.toInt() }
        scannerPoints[curScanner].add(Point(parsedPoint[0], parsedPoint[1], parsedPoint[2]))
    }

    val scanners = scannerPoints.filter { it.isNotEmpty() }.mapIndexed { ix, value -> BScanner(ix, value) }

    val stack = ArrayDeque<BScanner>()
    val visited = mutableListOf<Int>()
    stack.push(scanners[0])

    while(stack.isNotEmpty()) {
        val currentScanner = stack.pop()
        if (currentScanner.id in visited) {
            continue
        }

        visited.add(currentScanner.id)
        val pivotVectors = getVectors(currentScanner.points)

        for (nextScanner in scanners.filter { it.id !in visited }) {
            val transformed = compareScanners(nextScanner, pivotVectors)
            if (transformed != null) {
                nextScanner.points = transformed.first
                nextScanner.position = transformed.second
                stack.push(nextScanner)
            }
        }

    }

    println("Part 1:")
    println(scanners.map { it.points }.fold(setOf<Point>()) { s, points -> s.union(points) }.size)

    val maxDist =
        scanners.map { s1 -> scanners.map { s2 -> s1.position.manhattanDistance(s2.position) } }.flatten().maxOrNull()

    println("Part 2:")
    println(maxDist)
}

fun compareScanners(nextScanner: BScanner, pivotVectors: Map<Int, Pair<Point, Point>>): Pair<List<Point>, Point>? {
    for (xRot in 0..3) {
        for (yRot in 0..3) {
            for (zRot in 0..3) {
                val rotated = nextScanner
                    .points
                    .map { it.rotX(xRot * 90).rotY(yRot * 90).rotZ(zRot * 90) }
                val vectors = getVectors(rotated)
                val overlappingPoints = mutableSetOf<Point>()
                val keyIntersect = pivotVectors.keys.intersect(vectors.keys)
                for (vectorHash in keyIntersect) {
                    overlappingPoints.add(pivotVectors[vectorHash]!!.first)
                    overlappingPoints.add(pivotVectors[vectorHash]!!.second)
                }
                if (overlappingPoints.size >= 12) {
                    val matchedHash = keyIntersect.first()

                    val pivotVector = pivotVectors[matchedHash]!!
                    val pivotPoint = pivotVector.first

                    val matchedVector = vectors[matchedHash]!!
                    val matchedPoint = matchedVector.first

                    val deltaX = pivotPoint.x - matchedPoint.x
                    val deltaY = pivotPoint.y - matchedPoint.y
                    val deltaZ = pivotPoint.z - matchedPoint.z
                    val deltaPoint = Point(deltaX, deltaY, deltaZ)

                    return Pair(rotated.map { it.plus(deltaPoint) }, deltaPoint)
                }
            }
        }
    }

    return null
}

