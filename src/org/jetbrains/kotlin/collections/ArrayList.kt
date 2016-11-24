package org.jetbrains.kotlin.collections


class ArrayList<E> private constructor(
        private var a: Array<E>,
        private var ofs: Int = 0,
        private var len: Int = 0
) : MutableList<E> {

    constructor(initialCapacity: Int = 10) : this(arrayOfLazyInitElements(initialCapacity))

    constructor(c: Collection<E>) : this(c.size) {
        addAll(c)
    }

    override val size : Int
        get() = len
    
    override fun isEmpty(): Boolean = len == 0

    override fun get(index: Int): E {
        require(index in indices())
        return a[ofs + index]
    }

    override fun set(index: Int, element: E): E {
        require(index in indices())
        val old = a[ofs + index]
        a[ofs + index] = element
        return old
    }

    override fun contains(element: E): Boolean = indices().any { index -> a[index] == element }
    override fun containsAll(elements: Collection<E>): Boolean = elements.all { contains(it) }
    override fun indexOf(element: E): Int = indices().firstOrNull { index -> a[index] == element } ?: -1
    override fun lastIndexOf(element: E): Int = indices().reversed().firstOrNull { index -> a[index] == element } ?: -1

    override fun iterator(): MutableIterator<E> = Itr()
    override fun listIterator(): MutableListIterator<E> = Itr()

    override fun listIterator(index: Int): MutableListIterator<E> {
        require(index in itrIndices())
        return Itr(index)
    }

    override fun add(element: E): Boolean {
        ensureExtraCapacity()
        a[ofs + len++] = element
        return true
    }

    override fun add(index: Int, element: E) {
        insertAt(index)
        a[ofs + index] = element
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        val n = elements.size
        insertAt(index, n)
        val it = elements.iterator()
        for (i in 0..n - 1) a[ofs + index + i] = it.next()
        return n > 0
    }

    override fun addAll(elements: Collection<E>): Boolean = addAll(len, elements)

    override fun clear() {
        a.resetRange(ofs, ofs + len)
        len = 0
    }

    override fun removeAt(index: Int): E {
        require(index in indices())
        val old = a[ofs + index]
        a.copyRange(ofs + index + 1, ofs + len, ofs + index)
        a.resetAt(ofs + len - 1)
        len--
        return old
    }

    override fun remove(element: E): Boolean {
        val i = indexOf(element)
        if (i >= 0) removeAt(i)
        return i >= 0
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        var changed = false
        elements.forEach { if (remove(it)) changed = true }
        return changed
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        TODO("not implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
        require(fromIndex in itrIndices())
        require(toIndex in fromIndex..len)
        return ArrayList(a, ofs + fromIndex, toIndex - fromIndex) // todo: mark as sublist...
    }

    fun trimToSize() {
        if (len < a.size)
            a = a.copyOfLazyInitElements(len)
    }

    fun ensureCapacity(capacity: Int) {
        if (capacity > a.size)
            a = a.copyOfLazyInitElements(capacity.coerceAtLeast(a.size * 3 / 2))
    }

    override fun equals(other: Any?): Boolean {
        return other === this ||
            (other is List<*>) &&
            (other.size == len) &&
            indices().all { index -> other[index] == a[ofs + index] }
    }

    override fun hashCode(): Int {
        var result = 1
        indices().forEach { index -> result = result * 31 + (a[ofs + index]?.hashCode() ?: 0) }
        return result
    }

    override fun toString(): String = StringBuilder(2 + len * 3).apply {
        append("[")
        indices().forEach { index ->
            if (index > 0) append(", ")
            append(a[ofs + index])
        }
        append("]")
    }.toString()

    // ---------------------------- private ----------------------------

    private fun ensureExtraCapacity(n: Int = 1) {
        ensureCapacity(len + n)
    }

    private fun insertAt(index: Int, n: Int = 1) {
        ensureExtraCapacity(n)
        a.copyRange(ofs + index, ofs + size, ofs + index + n)
        len += n
    }

    private fun indices() = 0..len - 1
    private fun itrIndices() = 0..len

    private inner class Itr(private var index: Int = 0) : MutableListIterator<E> {
        private var lastIndex: Int = -1

        override fun hasPrevious(): Boolean = index > 0
        override fun hasNext(): Boolean = index < len

        override fun previousIndex(): Int = index - 1
        override fun nextIndex(): Int = index

        override fun previous(): E {
            check(index > 0)
            lastIndex = --index
            return a[ofs + lastIndex]
        }

        override fun next(): E {
            check(index < len)
            lastIndex = index++
            return a[ofs + lastIndex]
        }

        override fun set(element: E) {
            require(lastIndex in indices())
            a[ofs + lastIndex] = element
        }

        override fun add(element: E) {
            TODO("not implemented")
        }

        override fun remove() {
            TODO("not implemented")
        }
    }
}
