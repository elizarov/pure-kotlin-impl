package org.jetbrains.kotlin.collections

@Suppress("UNCHECKED_CAST")
class ArrayList<E> private constructor(
        private var a: Array<E?>,
        private var ofs: Int,
        private var len: Int
) : MutableList<E> {

    constructor(initialCapacity: Int = 10) : this(
            arrayOfNulls<Any>(initialCapacity) as Array<E?>, 0, 0)  // todo: unsafe cast

    override val size : Int
        get() = len
    
    override fun isEmpty(): Boolean = len == 0

    override fun get(index: Int): E {
        require(index in indices())
        return a[ofs + index] as E
    }

    override fun set(index: Int, element: E): E {
        require(index in indices())
        val old = a[ofs + index] as E
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
        require(index in iterIndices())
        return Itr(index)
    }

    override fun add(element: E): Boolean {
        checkResize()
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

    override fun addAll(elements: Collection<E>): Boolean = addAll(len - 1, elements)

    override fun clear() {
        for (i in indices()) a[ofs + i] = null
        len = 0
    }

    override fun removeAt(index: Int): E {
        require(index in indices())
        val old = a[ofs + index] as E
        a[ofs + index] = null
        for (i in index..len - 2) a[ofs + i] = a[ofs + i + 1]
        a[ofs + len - 1] = null
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
        require(fromIndex in iterIndices())
        require(toIndex in fromIndex..len)
        return ArrayList(a, ofs + fromIndex, toIndex - fromIndex) // todo: mark as sublist...
    }

    private fun checkResize(n: Int = 1) {
        val newSize = len + n
        if (newSize > a.size)
            a = a.copyOf(newSize.coerceAtLeast(len * 3 / 2)) // todo: copyOf jvm only
    }

    private fun insertAt(index: Int, n: Int = 1) {
        checkResize(n)
        for (i in len - 1 downTo index) // todo: more efficient array copy?
            a[ofs + i + n] = a[ofs + i]
    }

    private fun indices() = 0..len - 1
    private fun iterIndices() = 0..len

    private inner class Itr(private var index: Int = 0) : MutableListIterator<E> {
        private var lastIndex: Int = -1

        override fun hasPrevious(): Boolean = index > 0
        override fun hasNext(): Boolean = index < len

        override fun previousIndex(): Int = index - 1
        override fun nextIndex(): Int = index

        override fun previous(): E {
            check(index > 0)
            lastIndex = --index
            return a[ofs + lastIndex] as E
        }

        override fun next(): E {
            check(index < len)
            lastIndex = index++
            return a[ofs + lastIndex] as E
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
