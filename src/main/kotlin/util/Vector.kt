package util

class Vector<T : Number>(vararg values: T) {
        val numbers : Array<Number> = values.map{it}.toTypedArray()



   /*
    operator fun plus(other: Vector<T>) : Vector<T>{
        if (other.numbers.size == numbers.size) throw IllegalArgumentException("Vectors must match in Dimensions")
        val x: Array<T> = numbers.zip(other.numbers).map { (it.first.toDouble() + it.second.toDouble()) as T}.toTypedArray()
        return Vector(*x)
    }
        */

}