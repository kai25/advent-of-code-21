const val START_EDGE = "start"
const val END_EDGE = "end"

fun shouldVisitOnce(edge: String, path: List<String>): Boolean {
    return !(edge.toLowerCase() == edge && edge in path)
}

fun searchInDepth(
    start: String,
    edges: Map<String, List<String>>,
    currentPath: List<String>,
    shouldVisit: (String, List<String>) -> Boolean,
): List<List<String>> {
    if (start == END_EDGE) {
        return listOf(currentPath)
    }

    val nextEdges = edges.getOrDefault(start, listOf())

    val paths = mutableListOf<List<String>>()

    for (edge in nextEdges) {
        if (!shouldVisit(edge, currentPath)) {
            continue
        }

        paths.addAll(searchInDepth(edge, edges, currentPath + edge, shouldVisit))
    }

    return paths
}


fun solveDay12Part1() {
    val inputLines = readInput("day12_test.txt")
    val map: MutableMap<String, MutableList<String>> = mutableMapOf()

    for (line in inputLines) {
        val (a, b) = line.split("-").filter { it.isNotEmpty() }
        map.getOrPut(a) { mutableListOf() }.add(b)
        map.getOrPut(b) { mutableListOf() }.add(a)
    }

    val paths = searchInDepth(START_EDGE, map, listOf(START_EDGE)) { edge, path -> shouldVisitOnce(edge, path) }
    println(paths)
}


fun shouldVisitOnceTwice(edge: String, path: List<String>): Boolean {
    if (edge == START_EDGE && edge in path) {
        return false
    }

    if (edge == END_EDGE) {
        return true
    }

    if (edge.toLowerCase() != edge) {
        return true
    }

    if (edge !in path) {
        return true
    }

    val grouped = path.filter { it.toLowerCase() == it }.groupingBy { it }.eachCount()
    if (grouped.values.any { it > 1 }) {
        return false
    }

    return true
}


fun solveDay12Part2() {
    val inputLines = readInput("day12_test.txt")
    val map: MutableMap<String, MutableList<String>> = mutableMapOf()

    for (line in inputLines) {
        val (a, b) = line.split("-").filter { it.isNotEmpty() }
        map.getOrPut(a) { mutableListOf() }.add(b)
        map.getOrPut(b) { mutableListOf() }.add(a)
    }

    val paths = searchInDepth(START_EDGE, map, listOf(START_EDGE)) { edge, path -> shouldVisitOnceTwice(edge, path) }.map { it.joinToString(",") }.sorted()
    println(paths)
}