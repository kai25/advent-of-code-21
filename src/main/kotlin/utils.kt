import java.io.File
import java.nio.file.Paths

fun getCurrentPath(): String = Paths.get("").toAbsolutePath().toString()

fun readInput(name: String): List<String> {
    val file = File("${getCurrentPath()}/input", name)
    return file.readLines().filter { it.isNotEmpty() }
}

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}