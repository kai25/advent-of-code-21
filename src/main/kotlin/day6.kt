const val FISH_CYCLE_LEN = 6
const val NEW_FISH_CYCLE_LEN = 8


class FishGeneration(var cycleDay: Int, var fishCount: Long) {
    fun nextDay(): FishGeneration? {
        if (cycleDay == 0) {
            cycleDay = FISH_CYCLE_LEN
            return FishGeneration(NEW_FISH_CYCLE_LEN, fishCount)
        } else {
            cycleDay -= 1
            return null
        }
    }
}

fun solveDay6Part1() {
    val values = readInput("day6_test.txt")
        .first()
        .split(",")
        .map { it.toInt() }

    val generationMap: MutableMap<Int, FishGeneration> = HashMap()
    for (value in values) {
        val gen = generationMap.getOrDefault(value, FishGeneration(value, 0))
        gen.fishCount += 1
        generationMap[value] = gen
    }

    val generations: ArrayList<FishGeneration> = ArrayList()
    generations.addAll(generationMap.values)

    for (i in 0..255) {
        println("Day $i - ${generations.size}")
        val newGeneration = FishGeneration(NEW_FISH_CYCLE_LEN, 0)

        for (gen in generations) {
            val newGen = gen.nextDay()
            if (newGen != null) {
                newGeneration.fishCount += newGen.fishCount
            }
        }

        generations.add(newGeneration)
    }

    println(generations.sumOf { it.fishCount })
}