package control.remoteclient

import com.beust.klaxon.Klaxon
import control.DocumentController
import control.remoteclient.events.Event
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import util.Point
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class ConnectionController(private val documentController: DocumentController) {
    private val socket: Socket? = null
    private var readChannel: DataInputStream? = null
    private var writeChannel: DataOutputStream? = null

     fun connect(addr: String = "127.0.0.1", port: Int = 9002, sessionID: String, name: String) {

        val socket = Socket(addr, port)

        readChannel = DataInputStream(socket.getInputStream())
        writeChannel = DataOutputStream(socket.getOutputStream())

        val connectionPayload = "{\"type\": \"join\",\"sessionID\": \"$sessionID\",\"name\": \"$name\"}\n"

         ConcurrentExecutionController.scheduleJob {
                withContext(Dispatchers.IO) {
                    send(connectionPayload)
                }

                while (isActive) {
                    try {
                        // awaiting message
                        val msg = readChannel?.readUTF()

                        if (msg.isNullOrBlank()) continue
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

    fun newStroke(){
        send("{\"type\": \"newStroke\"}")
    }

    fun addPointToStroke(point: Point){
        val x = point.x
        val y = point.y
//        Json.encodeToString()
        send("{\"type\": \"draw\",\"x\":$x,\"y\":$y}")
    }


}