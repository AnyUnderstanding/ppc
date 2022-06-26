package control.remoteclient

import com.beust.klaxon.Klaxon
import control.DocumentController
import control.remoteclient.events.Event
import kotlinx.coroutines.*
import util.Point
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class ConnectionController(private val documentController: DocumentController) {
    private val socket: Socket? = null
    private var readChannel: DataInputStream? = null
    private var writeChannel: DataOutputStream? = null

    fun connect(
        addr: String = "192.168.1.203",
        port: Int = 9002,
        sessionID: String,
        name: String,
        createSession: Boolean = false
    ) {

        val socket = Socket(addr, port)

        readChannel = DataInputStream(socket.getInputStream())
        writeChannel = DataOutputStream(socket.getOutputStream())
        val connectionData: String = if (createSession) {
            "{\"type\": \"createSession\",\"name\": \"$name\"}"

        }else {
            "{\"type\": \"join\",\"sessionID\": \"$sessionID\",\"name\": \"$name\"}"
        }

        ConcurrentExecutionController.scheduleJob {
            withContext(Dispatchers.IO) {
                send(connectionData)
            }

            while (isActive) {
                try {
                    // awaiting message
                    val msg = readChannel?.readUTF()

                    if (msg.isNullOrBlank()) continue
                    println(msg)

                    Klaxon().parse<Event>(msg)?.handle(documentController)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun send(msg: String) {
        ConcurrentExecutionController.scheduleJob {
            writeChannel?.writeUTF(msg)
            writeChannel?.flush()
        }
    }

    fun newStroke(color: String, pageUUID: String, strokeUUID: String) {
        send("{\"type\": \"newStroke\", \"color\": \"$color\", \"pageUUID\":\"$pageUUID\", \"strokeUUID\": \"$strokeUUID\" }")
    }

    fun addPointToStroke(point: Point) {
        val x = point.x
        val y = point.y
//        Json.encodeToString()
        send("{\"type\": \"draw\",\"x\":$x,\"y\":$y}")
    }

    fun documentRequest() {
        send("{\"type\": \"docRequest\"}")
    }


}