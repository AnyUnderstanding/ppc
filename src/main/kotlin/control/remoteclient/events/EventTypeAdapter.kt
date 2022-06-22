package control.remoteclient.events

import com.beust.klaxon.TypeAdapter
import kotlin.reflect.KClass

class EventTypeAdapter : TypeAdapter<Event> {
    override fun classFor(type: Any): KClass<out Event> = when(type as String) {
        "join" -> JoinEvent::class
        "draw" -> DrawEvent::class
        "newStroke" -> NewStrokeEvent::class
        else -> throw UnkownEventException("Unknown event: $type")
    }
}
