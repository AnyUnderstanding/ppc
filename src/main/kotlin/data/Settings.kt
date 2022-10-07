@file:OptIn(ExperimentalSerializationApi::class)

package data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.serializer.MutableStateSerializer
import data.serializer.PathSerializer
import data.serializer.PropertiesSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromMap
import kotlinx.serialization.properties.decodeFromStringMap
import kotlinx.serialization.properties.encodeToStringMap
import ui.THEMES
import ui.Theme
import java.nio.file.Path
import kotlin.io.path.*
import java.util.Properties as JavaProperties

class Settings {
    private lateinit var settings: SettingsData

    //    private lateinit var latest: LatestData
    lateinit var settingsDir: Path
        private set

    private lateinit var settingsFile: Path
    private lateinit var serializer: PropertiesSerializer

    var theme: Theme
        get() = settings.theme.value.theme
        set(value) {
            settings.theme.value = THEMES.valueOf(value.toString())
        }

    var ppcHome: Path
        get() = settings.ppcHome.value
        set(value) {
            settings.ppcHome.value = value
        }

    var saveDelay: Int
        get() = settings.saveDelay.value
        set(value) {
            settings.saveDelay.value = value
        }

    init {
        getSettingsPath()
        init()
        println(settings)
    }

    private fun getSettingsPath() {

        val os = System.getProperty("os.name").lowercase()
        val dir = if (os.contains("win"))
            Path(System.getenv("APPDATA"))
        else
            Path(System.getProperty("user.home"), ".config/")

        settingsDir = Path(dir.pathString, "ppc")
        if (!settingsDir.exists()) settingsDir.createDirectories()

        settingsFile = Path.of(settingsDir.pathString, "options.txt")
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun read() {
        val props = PropertiesSerializer.import(settingsFile)
        settings = Properties.decodeFromStringMap(props)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun init() {
        if (!settingsFile.exists()) {
            settingsFile.createFile()
            val props = Properties.encodeToStringMap(SettingsData())
            PropertiesSerializer.export(settingsFile, props)
        }
        read()
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun save() {
        val props = Properties.encodeToStringMap(settings)
        PropertiesSerializer.export(settingsFile, props)
    }
}

@Serializable
data class SettingsData(
    @Serializable(with = MutableStateSerializer::class)
    val theme: MutableState<THEMES> = mutableStateOf(THEMES.LightTheme),

    @Serializable(with = MutableStateSerializer::class)
    val ppcHome: MutableState<@Serializable(with = PathSerializer::class) Path> = mutableStateOf(
        Path(
            System.getProperty(
                "user.home"
            ), "ppc"
        )
    ),

    @Serializable(with = MutableStateSerializer::class)
    val saveDelay: MutableState<Int> = mutableStateOf(10)
)

//@Serializable
//data class LatestData(
//    @Serializable(with = MutableStateSerializer::class)
//    val latestDocPath: MutableState<@Serializable(with = PathSerializer::class) Path?> = mutableStateOf(null),
//)

