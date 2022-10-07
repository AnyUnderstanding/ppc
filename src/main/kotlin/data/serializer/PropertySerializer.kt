package data.serializer

import java.nio.file.Path
import java.util.*
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

// Modified https://stackoverflow.com/a/63494719
object PropertiesSerializer {
    fun export(path: Path, data: Map<String, String>) {
        val props = data.toProperties()
        path.outputStream().use { props.store(it, "PPC Options V1") }
    }

    fun import(path: Path): Map<String, String> {
        val props = Properties()
        path.inputStream().use { props.load(it) }
        // NOTE: .map { (key, value) -> key.toString() to value.toString() }.toMap()
        return props.toMap() as Map<String, String>
    }
}