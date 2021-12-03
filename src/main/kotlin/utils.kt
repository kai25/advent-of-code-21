import java.io.File
import java.nio.file.Paths

fun getCurrentPath(): String = Paths.get("").toAbsolutePath().toString()

fun readInput(name: String): List<String> {
    val file = File("${getCurrentPath()}/input", name)
    return file.readLines().filter { it.isNotEmpty() }
}
