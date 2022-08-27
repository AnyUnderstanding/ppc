@file:OptIn(ExperimentalSerializationApi::class)

package data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import data.serializer.MutableStateSerializer
import data.serializer.PathSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import kotlinx.serialization.hocon.encodeToConfig
import java.io.File
//import kotlinx.serialization.properties.Properties
//import kotlinx.serialization.properties.encodeToStringMap
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.*

class Settings {
    lateinit var settings: SettingsData
    lateinit var settingsDir: Path
    lateinit var settingsFile: Path

    var darkmode: Boolean
        get() = settings.darkmode.value
        set(value) {
            settings.darkmode.value = value
        }

    var ppcHome: Path
        get() = settings.ppcHome.value
        set(value) {
            settings.ppcHome.value = value
        }

    init {
        getSettingsPath()
        save()
        read()
        println(settings)
    }

    private fun getSettingsPath() {

        val os = System.getProperty("os.name").lowercase()
        val dir = if (os.contains("win")) {
            Path.of(System.getenv("APPDATA"))
        } else {
            Path.of(System.getProperty("user.home"), ".config/")
        }

        settingsDir = Path(dir.pathString, "ppc")
        if (!settingsDir.exists()) settingsDir.createDirectory()
        settingsFile = Path.of(settingsDir.pathString, "ppc.conf")

    }

    private fun read() {
        if (!settingsFile.exists()) settingsFile.createFile()

        // Decode
        val conf = ConfigFactory.parseFile(settingsFile.toFile())
        settings = Hocon.decodeFromConfig(conf)
    }

    fun save() {
        if (!settingsFile.exists()) settingsFile.createFile()

        val s = SettingsData()
        s.darkmode.value = true

        val config = Hocon.encodeToConfig(s)
        val renderConfig = ConfigRenderOptions.concise()
        renderConfig.setJson(false)
        val output = config.root().render(renderConfig)


        settingsFile.writeText(output)
    }
}

@Serializable
data class SettingsData(
    @Serializable(with = MutableStateSerializer::class)
    val darkmode: MutableState<Boolean> = mutableStateOf(false),

    @Serializable(with = MutableStateSerializer::class)
    var ppcHome: MutableState<@Serializable(with = PathSerializer::class) Path> = mutableStateOf(
        Path(
            System.getProperty(
                "user.home"
            ), "ppc"
        )
    ),
)