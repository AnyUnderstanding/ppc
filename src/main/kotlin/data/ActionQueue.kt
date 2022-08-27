package data

import java.util.LinkedList
//import java.util.ArrayDeque
import kotlin.collections.ArrayDeque

class ActionQueue {
    private val undo = ArrayDeque<Action>()
    private val redo = LinkedList<Action>()

    //  --------------
    //         ^

    fun addAction(action: Action) {
        undo.addFirst(action)
        redo.clear()
        println("new action")
    }

    fun undo(): Action? {
        println("undo")

        runCatching {

            val action = undo.removeFirst()

            redo.add(action)
            return action
        }
        return null
    }

    fun redo(): Action? {
        println("redo")

        runCatching {
            val action = redo.removeLast()
            undo.addFirst(action)
            return action
        }
        return null
    }


}

abstract class Action

class DrawAction(val strokes: List<Stroke>, val strokeHolder: StrokeHolder) : Action()
class DeleteAction(val strokes: List<Stroke>, val strokeHolder: StrokeHolder) : Action()