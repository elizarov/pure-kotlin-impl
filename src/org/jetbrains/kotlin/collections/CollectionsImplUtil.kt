package org.jetbrains.kotlin.collections

import org.jetbrains.kotlin.text.StringBuilder

internal fun <E> Collection<E>.collectionToString(): String {
    val sb = StringBuilder(2 + size * 3)
    sb.append("[")
    var i = 0
    val it = iterator()
    while (it.hasNext()) {
        if (i > 0) sb.append(", ")
        val next = it.next()
        if (next == this) sb.append("(this Collection)") else sb.append(next)
        i++
    }
    sb.append("]")
    return sb.toString()
}
