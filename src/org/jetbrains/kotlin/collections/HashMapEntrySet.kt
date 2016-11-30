package org.jetbrains.kotlin.collections

internal class HashMapEntrySet<K, V> internal constructor(
        val backing: HashMap<K, V>
) : MutableSet<MutableMap.MutableEntry<K, V>> {

    override val size: Int get() = backing.size
    override fun isEmpty(): Boolean = backing.isEmpty()
    override fun contains(element: MutableMap.MutableEntry<K, V>): Boolean = backing.containsEntry(element)
    override fun clear() = backing.clear()
    override fun add(element: MutableMap.MutableEntry<K, V>): Boolean = backing.putEntry(element)
    override fun remove(element: MutableMap.MutableEntry<K, V>): Boolean = backing.removeEntry(element)
    override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> = backing.entriesIterator()
    override fun containsAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean = backing.containsAllEntries(elements)
    override fun addAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean = backing.putAllEntries(elements)
    override fun removeAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean = backing.removeAllEntries(elements)
    override fun retainAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean = backing.retainAllEntries(elements)

    override fun equals(other: Any?): Boolean =
        other === this ||
        other is Set<*> &&
        contentEquals(other)

    override fun hashCode(): Int {
        var result = 0
        val it = iterator()
        while (it.hasNext()) {
            result += it.next().hashCode()
        }
        return result
    }

    override fun toString(): String = collectionToString()

    // ---------------------------- private ----------------------------

    private fun contentEquals(other: Set<*>): Boolean {
        @Suppress("UNCHECKED_CAST") // todo: get rid of unchecked cast here somehow
        return size == other.size && backing.containsAllEntries(other as Collection<Map.Entry<*, *>>)
    }
}