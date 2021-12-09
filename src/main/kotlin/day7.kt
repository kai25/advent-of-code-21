import kotlin.math.abs

fun solveDay7() {
    val values = readInput("day7.txt")
        .first().split(",")
        .filter { it.isNotEmpty() }.map { it.toInt() }

    var min: Int = Int.MAX_VALUE
    for (ix in values.indices) {
        val f = getFuel(values, ix)
        if (f < min) {
            println("New min = $f at pos $ix")
            min = f
        }
    }
}


fun getFuel(values: List<Int>, targetPos: Int): Int {
    return values.sumOf { (1 .. abs(it - targetPos)).sum() }
}
