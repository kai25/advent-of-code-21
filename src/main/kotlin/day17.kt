import kotlin.math.max
import kotlin.math.min

class Target(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
    fun hit(x: Int, y: Int): Boolean {
        return x in minX..maxX && y in minY..maxY
    }
}

fun calculateX(v: Int, t: Int): Int {
    var x = 0
    var curV = v

    for (ix in 1..t) {
        x += curV
        curV = max(curV - 1, 0)
    }

    return x
}

fun calculateY(v: Int, t: Int): Pair<Int, Int> {
    var y = 0
    var curV = v
    var maxY = Int.MIN_VALUE

    for (ix in 1..t) {
        y += curV
        curV -= 1
        maxY = max(maxY, y)
    }

    return Pair(y, maxY)
}

fun calculateXVel(t: Int, target: Target): Pair<Int, Int> {
    val min = target.minX / t + t / 2
    val max = target.maxX / t + t / 2
    return Pair(min, max)
}

fun calculateYVel(t: Int, target: Target): Pair<Int, Int> {
    val min = target.minY / t + t / 2
    val max = target.maxY / t + t / 2
    return Pair(min, max)
}

fun getAppropriateSteps(target: Target): Pair<Int, Int> {
    var curT = 0
    var lastT = 0
    var minT = Int.MAX_VALUE
    while (true) {
        if (curT * curT / 2 > target.maxX) {
            break
        }
        if (curT * curT / 2 in target.minX..target.maxX) {
            lastT = curT
            minT = min(minT, lastT)
            curT += 1
            continue
        }
        curT += 1
    }

    return Pair(minT, lastT)
}


fun solveDay17Part1() {
    val input = readInput("day17.txt")
        .first()
        .split("=", ".", ",")
        .mapNotNull { it.toIntOrNull() }

    val (minX, maxX, minY, maxY) = input
    val target = Target(minX, maxX, minY, maxY)

    var maxT = 0
    var maxYV = 0
    var maxXV = 0
    var calculatedMaxY = 0

    for (t in 0..1000) {
        for (yv in 1..100) {
            val (y, mY) = calculateY(yv, t)
            if (y in target.minY..target.maxY) {
                calculatedMaxY = max(mY, calculatedMaxY)
                if (calculatedMaxY == mY) {
                    maxT = t
                    maxYV = yv
                }
            }
        }
    }

    for (xV in 0..1000) {
        if (calculateX(xV, maxT) in target.minX..target.maxX) {
            maxXV = xV
            break
        }
    }

    println("T=$maxT")
    println(Pair(maxXV, maxYV))
    println(calculatedMaxY)
}

fun solveDay17Part2() {
    val input = readInput("day17.txt")
        .first()
        .split("=", ".", ",")
        .mapNotNull { it.toIntOrNull() }

    val (minX, maxX, minY, maxY) = input
    val target = Target(minX, maxX, minY, maxY)

    val result = mutableSetOf<Pair<Int, Int>>()

    val X_STORAGE = (0..1000).map { t -> (0..1000).map { xv -> calculateX(xv, t) } }

    for (t in 0..1000) {
        for (yv in -100..100) {
            val (y, _) = calculateY(yv, t)
            if (y in target.minY..target.maxY) {
                for (xV in 0..1000) {
                    if (X_STORAGE[t][xV] in target.minX..target.maxX) {
                        result.add(Pair(xV, yv))
                    }
                }
            }
        }
    }

    println(result.count())
}