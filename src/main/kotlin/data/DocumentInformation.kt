package data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import util.Point
import java.nio.file.Path

open class Information(
    val name: String,
    val path: Path,
    val local: String = "",
    val parent: DirectoryInformation? = null
)

class DirectoryInformation(name: String, path: Path, local: String = "", parent: DirectoryInformation? = null) :
    Information(name, path, local, parent) {
    val files = mutableStateListOf<FileInformation>()
    val directories = mutableStateListOf<DirectoryInformation>()
    val open = mutableStateOf(false)
    val selected = mutableStateOf(true);

    override fun toString(): String {
        return "$path: $name - $local (${files.size} files, ${directories.size} directories)"
    }
}

class FileInformation(
    name: String,
    path: Path,
    local: String = "",
    parent: DirectoryInformation? = null,
    val size: Long = 0
) :
    Information(name, path, local, parent) {
    val selected = mutableStateOf(true);
    override fun toString(): String {
        return "$path: $name - $local ($size)"
    }
}