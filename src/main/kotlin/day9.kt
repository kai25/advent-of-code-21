fun solveDay9Part1() {
    val lines = readInput("day9_test.txt")

    val matrix: List<List<Int>> = lines
        .map { it.split("").filter { x -> x.isNotEmpty() }.map { x -> x.toInt() } }

    val MAX_Y = matrix.size
    val MAX_X = matrix.first().size
    val MAX_VAL = 10

    var s: Long = 0
    for (y in matrix.indices) {
        for (x in matrix[y].indices) {
            val center = matrix[y][x]

            val up = if (y == 0) MAX_VAL else matrix[y - 1][x]
            if (center >= up) {
                continue
            }
            val left = if (x == 0) MAX_VAL else matrix[y][x - 1]
            if (center >= left) {
                continue
            }
            val down = if (y == MAX_Y - 1) MAX_VAL else matrix[y + 1][x]
            if (center >= down) {
                continue
            }
            val right = if (x == MAX_X - 1) MAX_VAL else matrix[y][x + 1]
            if (center >= right) {
                continue
            }

            s += center + 1
        }
    }

    println(s)
}

fun solveDay9Part2() {
    val lines = readInput("day9_test.txt")

    val HIGH_POINT = 0
    val EMPTY_POINT = -1

    val matrix: MutableList<MutableList<Int>> = lines
        .map { it.split("")
            .filter { x -> x.isNotEmpty() }.map { x -> if (x.toInt() == 9) HIGH_POINT else EMPTY_POINT }.toMutableList() }
        .toMutableList()

    val MAX_Y = matrix.size
    val MAX_X = matrix.first().size
    val BASIN_SCORE: MutableMap<Int, Int> = HashMap()
    val CONNECTIONS: MutableSet<Pair<Int, Int>> = mutableSetOf()
    val MIN_BASIN_ID = 100
    var currentBasinId = MIN_BASIN_ID - 1

    for (y in matrix.indices) {
        currentBasinId += 1
        for (x in matrix[y].indices) {
            val curCel = matrix[y][x]

            if (curCel == HIGH_POINT) {
                currentBasinId += 1
                continue
            }

            val up = if (y == 0) HIGH_POINT else matrix[y - 1][x]
            if (up >= MIN_BASIN_ID) {
                if (up != currentBasinId) {
                    CONNECTIONS.add(Pair(up, currentBasinId))
                }
            }
            matrix[y][x] = currentBasinId
            val score = BASIN_SCORE.getOrDefault(currentBasinId, 0) + 1
            BASIN_SCORE[currentBasinId] = score
        }
    }

    val groups: ArrayList<MutableSet<Int>> = ArrayList()

    for ((a, b) in CONNECTIONS) {
        val matchedGroups = groups.filter { a in it || b in it }
        var group = mutableSetOf<Int>()

        if (matchedGroups.isNotEmpty()) {
            group = matchedGroups.first()
        } else {
            groups.add(group)
        }

        group.add(a)
        group.add(b)
    }

    val basinIdToGroup: Map<Int, Int> = groups.mapIndexed { index, s -> s.associateWith { index } }
        .flatMap { it.entries }
        .groupBy { it.key }
        .mapValues { entry -> entry.value.map { it.value }.first() }

    val groupSums = mutableMapOf<Int, Int>()
    for (y in matrix.indices) {
        for (x in matrix[y].indices) {
            val cell = matrix[y][x]
            if (cell >= MIN_BASIN_ID) {
                val group = basinIdToGroup[cell]
                var groupSum = groupSums.getOrDefault(group, 0)
                groupSum += 1
                if (group != null) {
                    groupSums[group] = groupSum
                }
            }
        }
    }

    val lastThree = groupSums.values.sorted().takeLast(3)

    println(lastThree[0] * lastThree[1] * lastThree[2])
}