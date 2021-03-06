package org.jetbrains.kotlin.text

class StringBuilder private constructor (
    private var array: CharArray
) : CharSequence {
    constructor() : this(10)

    constructor(capacity: Int) : this(CharArray(capacity))

    constructor(str: String) : this(str.toCharArray()) {
        length = array.size
    }

    override var length: Int = 0
        set(capacity) {
            ensureCapacity(capacity)
            field = capacity
        }

    override fun get(index: Int): Char {
        checkIndex(index)
        return array[index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = substring(startIndex, endIndex)

    override fun toString(): String = String(array, 0, length)

    fun substring(startIndex: Int, endIndex: Int): String {
        checkInsertIndex(startIndex)
        checkInsertIndexFrom(endIndex, startIndex)
        return String(array, startIndex, endIndex - startIndex)
    }

    fun trimToSize() {
        if (length < array.size)
            array = array.copyOf(length)
    }

    fun ensureCapacity(capacity: Int) {
        if (capacity > array.size) {
            var newSize = array.size * 3 / 2
            if (capacity > newSize)
                newSize = capacity
            array = array.copyOf(newSize)
        }
    }

    fun append(it: Char) {
        ensureExtraCapacity(1)
        array[length++] = it
    }

    fun append(it: CharArray) {
        ensureExtraCapacity(it.size)
        for (c in it)
            array[length++] = c
    }

    fun append(it: String) {
        ensureExtraCapacity(it.length)
        for (c in it)
            array[length++] = c
    }

    fun append(it: Boolean) = append(it.toString())
    fun append(it: Byte) = append(it.toString())
    fun append(it: Short) = append(it.toString())
    fun append(it: Int) = append(it.toString())
    fun append(it: Long) = append(it.toString())
    fun append(it: Float) = append(it.toString())
    fun append(it: Double) = append(it.toString())
    fun append(it: Any?) = append(it.toString())

    // ---------------------------- private ----------------------------

    private fun ensureExtraCapacity(n: Int) {
        ensureCapacity(length + n)
    }

    private fun checkIndex(index: Int) {
        if (index < 0 || index >= length) throw IndexOutOfBoundsException()
    }

    private fun checkInsertIndex(index: Int) {
        if (index < 0 || index > length) throw IndexOutOfBoundsException()
    }

    private fun checkInsertIndexFrom(index: Int, fromIndex: Int) {
        if (index < fromIndex || index > length) throw IndexOutOfBoundsException()
    }
}