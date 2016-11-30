package collections

import org.jetbrains.kotlin.collections.*
import java.util.*

class ArrayDeque<E>(initialCapacity: Int) : MutableCollection<E> {
    constructor() : this(16)

    private var array: Array<E> = arrayOfLateInitElements(powerOfTwo(initialCapacity))
    private var tail = 0
    private var head = 0
    private var full = false
    private var modCount = 0

    fun addLast(element: E): Boolean {
        ensureExtraCapacity(1)

        modCount++
        array[tail] = element
        tail = (tail + 1) and (array.size - 1)

        if (tail == head) {
            full = true
        }


        return true
    }

    fun addFirst(element: E): Boolean {
        ensureExtraCapacity(1)

        modCount++

        head = (head - 1) and (array.size - 1)
        array[head] = element

        if (tail == head) {
            full = true
        }

        return true
    }

    fun removeFirst(): E {
        if (isEmpty()) {
            throw NoSuchElementException()
        }

        return removeAt(head)
    }

    fun removeLast(): E {
        if (isEmpty()) {
            throw NoSuchElementException()
        }

        return removeAt((tail - 1) and (array.size - 1))
    }

    fun pollFirst(): E? {
        if (isEmpty()) return null
        return removeFirst()
    }

    fun pollLast(): E? {
        if (isEmpty()) return null
        return removeLast()
    }

    fun peekFirst(): E? {
        if (isEmpty()) return null
        return array[head]
    }

    fun peekLast(): E? {
        if (isEmpty()) return null
        return array[(tail - 1) and (array.size - 1)]
    }

    fun push(element: E) {
        addLast(element)
    }

    fun pop(): E = removeFirst()

    fun poll(): E? = if (isEmpty()) null else removeFirst()

    override fun add(element: E): Boolean {
        return addLast(element)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        if (elements.isEmpty()) {
            return false
        }

        ensureExtraCapacity(elements.size)

        for (e in elements) {
            addLast(e)
        }

        return true
    }

    override fun clear() {
        modCount++
        when {
            full -> array.resetRange(0, array.size)
            head < tail -> array.resetRange(head, tail)
            head > tail -> {
                array.resetRange(head, array.size)
                array.resetRange(0, tail)
            }
        }

        head = 0
        tail = 0
        full = false
    }

    override val size: Int
        get() = when {
            full -> array.size
            head < tail -> tail - head
            head > tail -> tail + (array.size - head)
            else -> 0
        }

    override fun isEmpty() = head == tail && !full

    override fun contains(element: E) = indexOf(element) != -1

    override fun containsAll(elements: Collection<E>): Boolean {
        for (e in elements) {
            if (e !in this) {
                return false
            }
        }

        return true
    }

    override fun iterator(): MutableIterator<E> {
        return IteratorImpl(this, modCount)
    }

    override fun remove(element: E): Boolean {
        val index = indexOf(element)

        if (index == -1) {
            return false
        }

        removeAt(index)
        return true
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        return inplaceFilter(elements, true)
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        return inplaceFilter(elements, false)
    }

    private fun inplaceFilter(elements: Collection<E>, contains: Boolean): Boolean {
        val it = iterator()
        var changed = false

        while (it.hasNext()) {
            val v = it.next()

            if (elements.contains(v) == contains) {
                it.remove()
                changed = true
            }
        }

        return changed
    }

    private fun removeAt(index: Int): E {
        val value = array[index]
        modCount++

        if (index == head) {
            array.resetAt(index)
            head = (head + 1) and (array.size - 1)
            full = false
        } else if (index == (tail - 1) and (array.size - 1)) {
            array.resetAt(index)
            tail = index
            full = false
        } else if (head < tail) {
            if (index < head || index >= tail) {
                throw IllegalArgumentException()
            }

            array.copyRangeTo(array, index + 1, tail, index)
            array.resetAt(tail - 1)
            tail--
        } else if (head > tail || full) {
            if (index < head) {
                array.copyRangeTo(array, index + 1, tail, index)
                array.resetAt(tail - 1)
                tail--
            } else {
                array.copyRangeTo(array, index + 1, array.size, index)
                if (tail > 0) {
                    array[array.size - 1] = array[0]
                    array.copyRangeTo(array, 1, tail, 0)
                }

                tail = (tail - 1) and (array.size - 1)
                array.resetAt(tail)
            }
            full = false
        } else {
            throw IllegalArgumentException()
        }

        return value
    }

    private fun indexOf(element: E): Int {
        if (isEmpty()) {
            return -1
        }

        var index = head
        val capacity = array.size
        var couldBeEqual = full

        do {
            if (array[index] == element) {
                return index
            }

            index++

            if (index == capacity) {
                index = 0
            }
            if (index == tail) {
                if (couldBeEqual) {
                    couldBeEqual = false
                } else {
                    break
                }
            }
        } while (true)

        return -1
    }

    private fun ensureExtraCapacity(n: Int) {
        if (n < 0) {
            throw IllegalArgumentException()
        }

        val space = when {
            full -> 0
            head <= tail -> array.size - tail + head
            else -> head - tail
        }

        if (n > space) {
            grow(array.size + (n - space))
        }
    }

    private fun grow(minSize: Int) {
        val newSize = size(minSize)
        val newArray = arrayOfLateInitElements<E>(newSize)
        modCount++

        when {
            head < tail -> {
                array.copyRangeTo(newArray, head, tail, 0)
                tail -= head
                head = 0
            }
            head > tail || full -> {
                array.copyRangeTo(newArray, 0, tail, (array.size - head))
                array.copyRangeTo(newArray, head, array.size, tail)
                tail += array.size - head
                head = 0
                full = false
            }
            else -> {
                // do nothing
            }
        }

        array = newArray
    }

    private fun size(minSize: Int): Int {
        var newSize = array.size

        while (newSize < minSize)
            newSize = newSize shl 1

        return newSize
    }

    private fun powerOfTwo(capacity: Int): Int {
        var s = 2
        while (s < capacity) {
            s = s shl 1
        }

        return s
    }

    override fun toString(): String {
        return joinToString(prefix = "[", postfix = "]")
    }

    private class IteratorImpl<E>(val owner: ArrayDeque<E>, var modCount: Int) : MutableIterator<E> {
        private var index = owner.head
        private var couldBeEqual = owner.full
        private var valueIndex = -1

        override fun remove() {
            assertModCount()
            if (valueIndex == -1) {
                throw IllegalStateException()
            }

            val index = valueIndex
            valueIndex = -1
            owner.removeAt(index)
            modCount = owner.modCount
            this.index = (this.index - 1) and (owner.array.size - 1)
        }

        override fun hasNext(): Boolean {
            assertModCount()
            return index != owner.tail || couldBeEqual
        }

        override fun next(): E {
            assertModCount()

            if (!hasNext()) {
                throw NoSuchElementException()
            }

            val result = owner.array[index]
            valueIndex = index

            if (index == owner.tail && couldBeEqual) {
                couldBeEqual = false
            }

            index = (index + 1) and (owner.array.size - 1)

            return result
        }

        private fun assertModCount() {
            if (modCount != owner.modCount) {
                throw ConcurrentModificationException()
            }
        }
    }
}