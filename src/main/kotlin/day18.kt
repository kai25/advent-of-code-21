import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

const val EXPLODE_DEPTH: Int = 5
const val SPLITTING_COUNT: Int = 10

class NumberNode(
    var left: NumberNode? = null, var right: NumberNode? = null, var value: Int? = null
) {
    var parent: NumberNode? = null

    fun connect(anotherNode: NumberNode): NumberNode {
        val parentNode = NumberNode(left=this, right=anotherNode)

        this.parent = parentNode
        anotherNode.parent = parentNode

        return parentNode
    }

    fun hasParent(): Boolean {
        return parent != null
    }

    fun isLeaf(): Boolean {
        return value != null
    }

    fun hasOnlyLeafs(): Boolean {
        return left != null && left!!.isLeaf() && right != null && right!!.isLeaf()
    }

    override fun toString(): String {
        if (isLeaf()) {
            return value.toString()
        } else {
            return "[${left.toString()}, ${right.toString()}]"
        }
    }
}

fun getLeftNeighbour(node: NumberNode): NumberNode? {
    var curNode = node
    while (true) {
        if (curNode.hasParent()) {
            val p = curNode.parent!!

            if (p.left == curNode) {
                curNode = p
                continue
            } else {
                curNode = p.left!!
                break
            }


        } else {
            return null
        }
    }

    if (curNode.isLeaf()) {
        return curNode
    }

    while (true) {
        if (curNode.right!!.isLeaf()) {
            return curNode.right
        } else {
            curNode = curNode.right!!
        }
    }
}

fun getRightNeighbour(node: NumberNode): NumberNode? {
    var curNode = node

    while (true) {
        if (curNode.hasParent()) {
            val p = curNode.parent!!

            if (p.right == curNode) {
                curNode = p
                continue
            } else {
                curNode = p.right!!
                break
            }


        } else {
            return null
        }
    }

    if (curNode.isLeaf()) {
        return curNode
    }

    while (true) {
        if (curNode.left!!.isLeaf()) {
            return curNode.left
        } else {
            curNode = curNode.left!!
        }
    }
}

fun hasExplodingNodes(node: NumberNode?, depth: Int = 1): Boolean {
    if (node == null) {
        return false
    }

    if (node.isLeaf()) {
        return false
    }

    if (node.hasOnlyLeafs() && depth >= EXPLODE_DEPTH) {
        return true
    }

    return hasExplodingNodes(node.left!!, depth + 1) || hasExplodingNodes(node.right!!, depth + 1)
}

fun hasSplittingNodes(node: NumberNode?): Boolean {
    if (node == null) {
        return false
    }

    if (node.isLeaf()) {
        return node.value!! >= SPLITTING_COUNT
    }

    return hasSplittingNodes(node.left) || hasSplittingNodes(node.right)
}


fun explodeNumber(node: NumberNode, depth: Int = 1) {
    if (node.left != null) {
        explodeNumber(node.left!!, depth + 1)
    }
    if (node.right != null) {
        explodeNumber(node.right!!, depth + 1)
    }

    if (node.hasOnlyLeafs() && depth >= EXPLODE_DEPTH) {
        val left = node.left!!.value!!
        val right = node.right!!.value!!

        val leftNeighbour = getLeftNeighbour(node)
        if (leftNeighbour?.value != null) {
            leftNeighbour.value = leftNeighbour.value!! + left
        }
        val rightNeighbour = getRightNeighbour(node)
        if (rightNeighbour?.value != null) {
            rightNeighbour.value = rightNeighbour.value!! + right
        }

        node.left = null
        node.right = null
        node.value = 0
    }
}

fun splitNumbers(node: NumberNode?): Boolean {
    if (node == null) {
        return false
    }

    if (node.value !== null) {
        if (node.value!! >= SPLITTING_COUNT) {
            val splittedNode = NumberNode(value=floor(node.value!! / 2.0).toInt())
                .connect(
                    NumberNode(value=ceil(node.value!! / 2.0).toInt())
                )

            splittedNode.parent = node.parent
            if (node.parent!!.left == node) {
                node.parent!!.left = splittedNode
            }

            if (node.parent!!.right == node) {
                node.parent!!.right = splittedNode
            }

            return true
        } else {
            return false
        }
    }
    if (splitNumbers(node.left)){
        return true
    }

    if (splitNumbers(node.right)){
        return true
    }

    return false
}

fun parseNumber(raw: String): NumberNode {
    if (raw.toIntOrNull() != null) {
        return NumberNode(value=raw.toIntOrNull())
    }

    var splitAt = -1

    var depth = 0
    for (ix in raw.indices) {
        val c = raw[ix]
        if (c == ']') {
            depth -= 1
        }
        else if (c == '[') {
            depth += 1
        }
        else if (c == ',') {
            if (depth == 1) {
                splitAt = ix
                break
            }
        }
    }

    val leftRaw = raw.slice(1 until splitAt).trim()
    val rightRaw = raw.slice(splitAt + 1 until raw.length - 1).trim()

    return parseNumber(leftRaw).connect(parseNumber(rightRaw))
}

fun calcScore(node: NumberNode?): Int {
    if (node == null) {
        return 0
    }
    if (node.isLeaf()) {
        return node.value!!
    }
    return calcScore(node.left) * 3 + calcScore(node.right) * 2
}

fun solveDay18Part1() {
    val inputLines = readInput("day18.txt")
    var number = parseNumber(inputLines.first())

    for (line in inputLines.drop(1)) {
        val parsed = parseNumber(line)
        number = number.connect(parsed)

        while(hasExplodingNodes(number) || hasSplittingNodes(number)) {
            explodeNumber(number)
            splitNumbers(number)
        }

        println(number.toString())
    }

    println(calcScore(number))
}

fun solveDay18Part2() {
    val inputLines = readInput("day18.txt")

    var maxScore = 0

    for (a in inputLines) {
        for (b in inputLines) {
            if (a == b) {
                continue
            }
            val an = parseNumber(a)
            val bn = parseNumber(b)
            val number = an.connect(bn)

            while(hasExplodingNodes(number) || hasSplittingNodes(number)) {
                explodeNumber(number)
                splitNumbers(number)
            }

            maxScore = max(maxScore, calcScore(number))
        }
    }

    println(maxScore)
}