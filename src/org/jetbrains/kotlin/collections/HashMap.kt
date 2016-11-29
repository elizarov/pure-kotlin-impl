package org.jetbrains.kotlin.collections

class HashMap<K, V> private constructor(
    private var keysArray: Array<K>,
    private var valuesArray: Array<V>,
    private var presenceArray: BooleanArray,
    private var hashArray: IntArray,
    private var maxProbes: Int,
    private var length: Int
) : MutableMap<K, V> {
    private var hashShift: Int = computeShift(hashSize)

    override var size: Int = 0
        private set

    // ---------------------------- functions ----------------------------

    constructor() : this(INITIAL_CAPACITY)

    constructor(capacity: Int) : this(
        arrayOfLateInitElements(capacity),
        arrayOfLateInitElements(capacity),
        BooleanArray(capacity),
        IntArray(computeHashSize(capacity)),
        INITIAL_MAX_PROBES,
        0)

    override fun isEmpty(): Boolean = size == 0

    fun ensureCapacity(capacity: Int) {
        if (capacity > this.capacity) {
            var newSize = this.capacity * 3 / 2
            if (capacity > newSize) newSize = capacity
            keysArray = keysArray.copyOfLateInitElements(newSize)
            valuesArray = valuesArray.copyOfLateInitElements(newSize)
            presenceArray = presenceArray.copyOf(newSize)
            val newHashSize = computeHashSize(newSize)
            if (newHashSize > hashSize) rehash(newHashSize)
        } else if (length + capacity - size > this.capacity) {
            rehash(hashSize)
        }
    }

    override fun containsKey(key: K): Boolean {
        var hash = hash(key)
        var probesLeft = maxProbes
        while (true)  {
            val index = hashArray[hash]
            if (index == 0) return false
            if (index > 0 && keysArray[index - 1] == key) return true
            if (--probesLeft == 0) return false
            if (hash-- == 0) hash = hashSize - 1
        }
    }

    override fun containsValue(value: V): Boolean {
        var i = length
        while (--i >= 0) {
            if (presenceArray[i] && valuesArray[i] == value)
                return true
        }
        return false
    }

    override fun get(key: K): V? {
        var hash = hash(key)
        var probesLeft = maxProbes
        while (true)  {
            val index = hashArray[hash]
            if (index == 0) return null
            if (index > 0 && keysArray[index - 1] == key) return valuesArray[index - 1]
            if (--probesLeft == 0) return null
            if (hash-- == 0) hash = hashSize - 1
        }
    }

    override fun put(key: K, value: V): V? {
        ensureExtraCapacity(1)
        return putInternal(key, value)
    }

    override fun putAll(from: Map<out K, V>) {
       ensureExtraCapacity(from.size)
       for ((key, value) in from) {
           putInternal(key, value)
       }
    }

    override fun remove(key: K): V? {
        var hash = hash(key)
        var probesLeft = maxProbes
        while (true)  {
            val index = hashArray[hash]
            if (index == 0) return null
            if (index > 0 && keysArray[index - 1] == key) {
                val oldValue = valuesArray[index - 1]
                keysArray.resetAt(index - 1)
                valuesArray.resetAt(index - 1)
                presenceArray[index - 1] = false
                hashArray[hash] = TOMBSTONE
                size--
                return oldValue
            }
            if (--probesLeft == 0) return null
            if (hash-- == 0) hash = hashSize - 1
        }
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = TODO("not implemented")

    override val keys: MutableSet<K>
        get() = TODO("not implemented")

    override val values: MutableCollection<V>
        get() = TODO("not implemented")

    override fun clear() {
        TODO("not implemented")
    }

    // ---------------------------- private ----------------------------

    private val capacity: Int get() = keysArray.size
    private val hashSize: Int get() = hashArray.size

    private fun ensureExtraCapacity(n: Int) {
        ensureCapacity(length + n)
    }

    private fun hash(key: K) = (key.hashCode() * MAGIC) ushr hashShift

    private fun compact() {
        TODO("")
    }

    private fun rehash(newHashSize: Int) {
        if (length > size) compact()
        var tryHashSize = newHashSize
        retry@ while (true) {
            if (tryHashSize != hashSize) {
                hashArray = IntArray(tryHashSize)
                hashShift = computeShift(tryHashSize)
            } else {
                hashArray.fill(0, 0, hashSize)
            }
            var i = 0
            while (i < length) {
                if (!putRehash(i++)) {
                    // todo: some smarter strategy/heuristic
                    tryHashSize *= 2
                    maxProbes *= 2
                    continue@retry
                }
            }
            return
        }
    }

    private fun putRehash(i: Int): Boolean {
        var hash = hash(keysArray[i])
        var probesLeft = maxProbes
        while (true) {
            val index = hashArray[hash]
            if (index == 0) {
                hashArray[hash] = i + 1
                return true
            }
            if (--probesLeft == 0) return false
            if (hash-- == 0) hash = hashSize - 1
        }
    }

    private fun putInternal(key: K, value: V): V? {
        retry@ while (true) {
            var hash = hash(key)
            var probesLeft = maxProbes
            while (true) {
                val index = hashArray[hash]
                if (index == 0) {
                    val putIndex = length++
                    keysArray[putIndex] = key
                    valuesArray[putIndex] = value
                    presenceArray[putIndex] = true
                    hashArray[hash] = putIndex + 1
                    size++
                    return null
                }
                if (index > 0 && keysArray[index - 1] == key) {
                    val oldValue = valuesArray[index - 1]
                    valuesArray[index - 1] = value
                    return oldValue
                }
                if (--probesLeft == 0) {
                    rehash(hashSize * 2) // todo: smarter strategy
                    continue@retry
                }
                if (hash-- == 0) hash = hashSize - 1
            }
        }
    }

    private companion object {
        const val MAGIC = 2654435769L.toInt() // golden ratio
        const val INITIAL_CAPACITY = 8
        const val INITIAL_MAX_PROBES = 5
        const val TOMBSTONE = -1

        fun computeHashSize(capacity: Int): Int = (capacity.coerceAtLeast(1) * 3).highestOneBit()

        fun computeShift(hashSize: Int): Int = hashSize.numberOfLeadingZeros() + 1
    }
}
