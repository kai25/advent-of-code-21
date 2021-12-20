class Octopus(var energy: Int, var x: Int, var y: Int) {
    var isFlashed: Boolean = false

    fun startStep() {
        energy += 1
    }

    fun endStep() {
        if (isFlashed) {
            energy = 0
        }
        isFlashed = false
    }

    fun markFlashed() {
        isFlashed = true
    }

    fun charge() {
        if (!isFlashed) {
            energy += 1
        }
    }

    fun isCharged(): Boolean {
        return energy > 9 && !isFlashed
    }

    override fun toString(): String {
        return "Octo(charge=${energy}, x=${x}, y=${y})"
    }
}

fun chargeAdjacentCells(cell: Octopus, matrix: List<List<Octopus>>) {
    val rowsRange = matrix.indices
    val colRange = matrix.first().indices

    val x = cell.x
    val y = cell.y

    if (x - 1 in colRange) {
        matrix[y][x - 1].charge()
    }
    if (x + 1 in colRange) {
        matrix[y][x + 1].charge()
    }
    if (y - 1 in rowsRange) {
        matrix[y - 1][x].charge()
    }
    if (y + 1 in rowsRange) {
        matrix[y + 1][x].charge()
    }
    if (x - 1 in colRange && y - 1 in rowsRange) {
        matrix[y - 1][x - 1].charge()
    }
    if (x + 1 in colRange && y - 1 in rowsRange) {
        matrix[y - 1][x + 1].charge()
    }
    if (x - 1 in colRange && y + 1 in rowsRange) {
        matrix[y + 1][x - 1].charge()
    }
    if (x + 1 in colRange && y + 1 in rowsRange) {
        matrix[y + 1][x + 1].charge()
    }
}

fun countOctoToBeFlashed(matrix: List<List<Octopus>>): Int {
    var counter = 0

    for (row in matrix) {
        for (cell in row) {
            if (cell.isCharged()) {
                counter += 1
            }
        }
    }

    return counter
}

fun solveDay11Part1() {
    val inputLines = readInput("day11_test.txt")
    val matrix = inputLines
        .mapIndexed { y, row -> row.split("")
            .filter { cell -> cell.isNotEmpty() }.mapIndexed { x, cell -> Octopus(cell.toInt(), x, y) } }

    var flashCounter = 0
    for (ix in 0..99) {
        for (row in matrix) {
            for (cell in row) {
                cell.startStep()
            }
        }

        while(countOctoToBeFlashed(matrix) > 0) {
            for (row in matrix) {
                for (cell in row) {
                    if (cell.isCharged()) {
                        chargeAdjacentCells(cell, matrix)
                        cell.markFlashed()
                        flashCounter += 1
                    }
                }
            }
        }

        for (row in matrix) {
            for (cell in row) {
                cell.endStep()
            }
        }
        println(ix)
    }

    println(flashCounter)
}

fun solveDay11Part2() {
    val inputLines = readInput("day11_test.txt")
    val matrix = inputLines
        .mapIndexed { y, row -> row.split("")
            .filter { cell -> cell.isNotEmpty() }.mapIndexed { x, cell -> Octopus(cell.toInt(), x, y) } }

    for (ix in 0..1000) {
        for (row in matrix) {
            for (cell in row) {
                cell.startStep()
            }
        }

        var stepCounter = 0

        while(countOctoToBeFlashed(matrix) > 0) {
            for (row in matrix) {
                for (cell in row) {
                    if (cell.isCharged()) {
                        chargeAdjacentCells(cell, matrix)
                        cell.markFlashed()
                        stepCounter += 1
                    }
                }
            }
        }

        for (row in matrix) {
            for (cell in row) {
                cell.endStep()
            }
        }
        if (stepCounter == 100) {
            println(ix + 1)
            break
        }
    }
}