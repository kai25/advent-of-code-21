class BoardItem(val value: Int, var isMarked: Boolean = false)

class Board(matrix: List<List<Int>>) {
    private val matrixSize = matrix.size
    private val rowsCounts: Array<Int> = Array(matrixSize) { 0 }
    private val colsCounts: Array<Int> = Array(matrixSize) { 0 }
    private val matrix = matrix.map { it.map { value -> BoardItem(value) } }
    var isCompleted = false

    fun markIfExists(value: Int): Boolean {
        for (rowIx in 0 until matrixSize) {
            for (colIx in 0 until matrixSize) {
                val item = matrix[rowIx][colIx]
                if (item.value == value && !item.isMarked) {
                    return markItem(item, rowIx, colIx)
                }
            }
        }

        return false
    }

    private fun markItem(item: BoardItem, rowIx: Int, colIx: Int): Boolean {
        item.isMarked = true
        rowsCounts[rowIx] += 1
        colsCounts[colIx] += 1


        if (isRowCompleted(rowIx) || isColumnCompleted(colIx)) {
            isCompleted = true
            return true
        }

        return false
    }

    private fun isRowCompleted(rowIx: Int): Boolean {
        return rowsCounts[rowIx] == matrixSize
    }

    private fun isColumnCompleted(colIx: Int): Boolean {
        return colsCounts[colIx] == matrixSize
    }

    fun calculateScore(lastValue: Int): Int {
        val sumOfUnmarked = matrix
            .sumOf { row ->
                row.sumOf { item -> if (item.isMarked) 0 else item.value }
            }

        return sumOfUnmarked * lastValue
    }
}

fun parseBoard(rawBoard: List<String>): Board {
    val matrix = rawBoard
        .map {
            it.split(" ")
                .filter { value -> value.isNotEmpty() }
                .map { value -> value.toInt() }
        }

    return Board(matrix)
}

fun parseInput(): Pair<List<Int>, List<Board>> {
    val inputLines = readInput("day4.txt")

    val numbers = inputLines.first().split(",").map { it.toInt() }

    val rawBoards = inputLines.slice(1 until inputLines.size)
    val boardSize = rawBoards.first().split(" ").filter { it.isNotEmpty() }.size
    val chunkedRawBoards = rawBoards.chunked(boardSize)
    val boards: List<Board> = chunkedRawBoards.map { parseBoard(it) }

    return Pair(numbers, boards)
}

fun solveDay4Part1() {
    val (numbers, boards) = parseInput()

    for (number in numbers) {
        for (board in boards) {
            val isCompleted = board.markIfExists(number)
            if (isCompleted) {
                println("Result is ${board.calculateScore(number)}")
                return
            }
        }
    }
}

fun solveDay4Part2() {
    val (numbers, boards) = parseInput()

    var boardsLeft = boards

    for (number in numbers) {
        for (board in boardsLeft) {
            board.markIfExists(number)
        }

        if (boardsLeft.size == 1 && boardsLeft.first().isCompleted) {
            println(boardsLeft.first().calculateScore(number))
            return
        }

        boardsLeft = boardsLeft.filter { !it.isCompleted }
    }
}