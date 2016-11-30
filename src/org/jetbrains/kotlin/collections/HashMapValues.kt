package org.jetbrains.kotlin.collections

internal class HashMapValues<V> internal constructor(
        val backing: HashMap<*, V>
) : MutableCollection<V> {

    override val size: Int get() = backing.size
    override fun isEmpty(): Boolean = backing.isEmpty()
    override fun contains(element: V): Boolean = backing.containsValue(element)
    override fun containsAll(elements: Collection<V>): Boolean = backing.containsAllValues(elements)
    override fun add(element: V): Boolean = throw UnsupportedOperationException()
    override fun addAll(elements: Collection<V>): Boolean = throw UnsupportedOperationException()
    override fun clear() = backing.clear()
    override fun iterator(): MutableIterator<V> = backing.valuesIterator()
    override fun remove(element: V): Boolean = backing.removeValue(element)
    override fun removeAll(elements: Collection<V>): Boolean = backing.removeAllValues(elements)
    override fun retainAll(elements: Collection<V>): Boolean = backing.retainAllValues(elements)

    override fun equals(other: Any?): Boolean =
        other === this ||
        other is Collection<*> &&
        contentEquals(other)

    override fun hashCode(): Int {
        var result = 1
        val it = iterator()
        while (it.hasNext()) {
            result = result * 31 + it.next().hashCode()
        }
        return result
    }

    override fun toString(): String = collectionToString()

    // ---------------------------- private ----------------------------

    private fun contentEquals(other: Collection<*>): Boolean {
        @Suppress("UNCHECKED_CAST") // todo: figure out something better
        return size == other.size && backing.containsAllValues(other as Collection<V>)
    }
}