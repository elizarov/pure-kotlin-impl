package org.jetbrains.kotlin.collections

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class HashMapTest {
    @Test
    fun testBasic() {
        val m = HashMap<String, String>()
        assertTrue(m.isEmpty())
        assertEquals(0, m.size)

        assertFalse(m.containsKey("1"))
        assertFalse(m.containsValue("a"))
        assertEquals(null, m.get("1"))

        assertEquals(null, m.put("1", "a"))
        assertTrue(m.containsKey("1"))
        assertTrue(m.containsValue("a"))
        assertEquals("a", m.get("1"))
        assertFalse(m.isEmpty())
        assertEquals(1, m.size)

        assertFalse(m.containsKey("2"))
        assertFalse(m.containsValue("b"))
        assertEquals(null, m.get("2"))

        assertEquals(null, m.put("2", "b"))
        assertTrue(m.containsKey("1"))
        assertTrue(m.containsValue("a"))
        assertEquals("a", m.get("1"))
        assertTrue(m.containsKey("2"))
        assertTrue(m.containsValue("b"))
        assertEquals("b", m.get("2"))
        assertFalse(m.isEmpty())
        assertEquals(2, m.size)

        assertEquals("b", m.put("2", "bb"))
        assertTrue(m.containsKey("1"))
        assertTrue(m.containsValue("a"))
        assertEquals("a", m.get("1"))
        assertTrue(m.containsKey("2"))
        assertTrue(m.containsValue("a"))
        assertTrue(m.containsValue("bb"))
        assertEquals("bb", m.get("2"))
        assertFalse(m.isEmpty())
        assertEquals(2, m.size)

        assertEquals("a", m.remove("1"))
        assertFalse(m.containsKey("1"))
        assertFalse(m.containsValue("a"))
        assertEquals(null, m.get("1"))
        assertTrue(m.containsKey("2"))
        assertTrue(m.containsValue("bb"))
        assertEquals("bb", m.get("2"))
        assertFalse(m.isEmpty())
        assertEquals(1, m.size)

        assertEquals("bb", m.remove("2"))
        assertFalse(m.containsKey("1"))
        assertFalse(m.containsValue("a"))
        assertEquals(null, m.get("1"))
        assertFalse(m.containsKey("2"))
        assertFalse(m.containsValue("bb"))
        assertEquals(null, m.get("2"))
        assertTrue(m.isEmpty())
        assertEquals(0, m.size)
    }

    @Test
    fun testRehashAndCompact() {
        val m = HashMap<String, String>()
        for (repeat in 1..10) {
            val n = when (repeat) {
                1 -> 1000
                2 -> 10000
                3 -> 10
                else -> 100000
            }
            for (i in 1..n) {
                assertFalse(m.containsKey(i.toString()))
                assertEquals(null, m.put(i.toString(), "val$i"))
                assertTrue(m.containsKey(i.toString()))
                assertEquals(i, m.size)
            }
            for (i in 1..n) {
                assertTrue(m.containsKey(i.toString()))
            }
            for (i in 1..n) {
                assertEquals("val$i", m.remove(i.toString()))
                assertFalse(m.containsKey(i.toString()))
                assertEquals(n - i, m.size)
            }
            assertTrue(m.isEmpty())
        }
    }

    @Test
    fun testRandomized() {
        checkRandomized(0.0)
    }

    @Test
    fun testRandomizedDegenerate1() {
        checkRandomized(0.01)
    }

    @Test
    fun testRandomizedDegenerate2() {
        checkRandomized(0.1)
    }

    @Test
    fun testRandomizedDegenerate3() {
        checkRandomized(0.5)
    }

    private fun checkRandomized(degeneteProb: Double) {
        val n = 1000000 // total number
        val minWindow = 10
        val maxWindow = 100
        val rnd = Random(1)
        val used = mutableSetOf<String>()
        val a = Array<String>(n) {
            var s: String
            do {
                s = "a${rnd.nextInt()}"
            } while (!used.add(s))
            s
        }
        if (degeneteProb > 0) for (i in 1..n - 1) if (rnd.nextDouble() < degeneteProb) {
            val sb = StringBuilder(a[rnd.nextInt(i)])
            var s: String
            do {
                degenerateUpdate(sb)
                s = sb.toString()
            } while (!used.add(s))
            a[i] = s
        }
        var head = 0
        var tail = 0
        val m = HashMap<String, String>()
        while (tail < n) {
            val doPut = when (head - tail) {
                in 0..minWindow - 1 -> head < n
                in minWindow..maxWindow -> head < n && rnd.nextBoolean()
                else -> false
            }
            if (doPut) {
                assertEquals(null, m.put(a[head], a[head]))
                head++
            } else {
                assertEquals(a[tail], m.remove(a[tail]))
                tail++
            }
            assertTrue(m.size <= maxWindow + 1)
        }
    }

    @Test
    fun testClear() {
        val m = HashMap<String, String>()
        for (repeat in 1..10) {
            val n = when (repeat) {
                1 -> 1000
                2 -> 10000
                3 -> 10
                else -> 100000
            }
            for (i in 1..n) {
                assertFalse(m.containsKey(i.toString()))
                assertEquals(null, m.put(i.toString(), "val$i"))
                assertTrue(m.containsKey(i.toString()))
                assertEquals(i, m.size)
            }
            for (i in 1..n) {
                assertTrue(m.containsKey(i.toString()))
            }
            m.clear()
            assertEquals(0, m.size)
            for (i in 1..n) {
                assertFalse(m.containsKey(i.toString()))
            }
        }
    }

    @Test
    fun testEquals() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        assertTrue(m == expected)
        assertTrue(m == mapOf("b" to "2", "c" to "3", "a" to "1"))  // order does not matter
        assertFalse(m == mapOf("a" to "1", "b" to "2", "c" to "4"))
        assertFalse(m == mapOf("a" to "1", "b" to "2", "c" to "5"))
        assertFalse(m == mapOf("a" to "1", "b" to "2"))
        assertEquals(m.keys, expected.keys)
        assertEquals(m.values, expected.values)
        assertEquals(m.entries, expected.entries)
    }

    @Test
    fun testHashCode() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        assertEquals(expected.hashCode(), m.hashCode())
        assertEquals(expected.entries.hashCode(), m.entries.hashCode())
        assertEquals(expected.keys.hashCode(), m.keys.hashCode())
        assertEquals(listOf("1", "2", "3").hashCode(), m.values.hashCode())
    }

    @Test
    fun testToString() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        assertEquals(expected.toString(), m.toString())
        assertEquals(expected.entries.toString(), m.entries.toString())
        assertEquals(expected.keys.toString(), m.keys.toString())
        assertEquals(expected.values.toString(), m.values.toString())
    }

    @Test
    fun testPutEntry() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        val e = expected.entries.iterator().next() as MutableMap.MutableEntry<String, String>
        assertTrue(m.entries.contains(e))
        assertTrue(m.entries.remove(e))
        assertTrue(mapOf("b" to "2", "c" to "3") == m)
        assertTrue(m.entries.add(e))
        assertTrue(expected == m)
        assertFalse(m.entries.add(e))
        assertTrue(expected == m)
    }

    @Test
    fun testRemoveAllEntries() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        assertFalse(m.entries.removeAll(mapOf("a" to "2", "b" to "3", "c" to "4").entries))
        assertEquals(expected, m)
        assertTrue(m.entries.removeAll(mapOf("b" to "22", "c" to "3", "d" to "4").entries))
        assertNotEquals(expected, m)
        assertEquals(mapOf("a" to "1", "b" to "2"), m)
    }

    @Test
    fun testRetainAllEntries() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        assertFalse(m.entries.retainAll(expected.entries))
        assertEquals(expected, m)
        assertTrue(m.entries.retainAll(mapOf("b" to "22", "c" to "3", "d" to "4").entries))
        assertEquals(mapOf("c" to "3"), m)
    }

    @Test
    fun testContainsALlValues() {
        val m = HashMap(mapOf("a" to "1", "b" to "2", "c" to "3"))
        assertTrue(m.values.containsAll(listOf("1", "2")))
        assertTrue(m.values.containsAll(listOf("1", "2", "3")))
        assertFalse(m.values.containsAll(listOf("1", "2", "3", "4")))
        assertFalse(m.values.containsAll(listOf("2", "3", "4")))
    }

    @Test
    fun testRemoveValue() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        assertFalse(m.values.remove("b"))
        assertEquals(expected, m)
        assertTrue(m.values.remove("2"))
        assertEquals(mapOf("a" to "1", "c" to "3"), m)
    }

    @Test
    fun testRemoveAllValues() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        assertFalse(m.values.removeAll(listOf("b", "c")))
        assertEquals(expected, m)
        assertTrue(m.values.removeAll(listOf("b", "3")))
        assertEquals(mapOf("a" to "1", "b" to "2"), m)
    }

    @Test
    fun testRetainAllValues() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        assertFalse(m.values.retainAll(listOf("1", "2", "3")))
        assertEquals(expected, m)
        assertTrue(m.values.retainAll(listOf("1", "2", "c")))
        assertEquals(mapOf("a" to "1", "b" to "2"), m)
    }

    @Test
    fun testEntriesIteratorSet() {
        val expected = mapOf("a" to "1", "b" to "2", "c" to "3")
        val m = HashMap(expected)
        val it = m.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            entry.setValue(entry.value + "!")
        }
        assertNotEquals(expected, m)
        assertEquals(mapOf("a" to "1!", "b" to "2!", "c" to "3!"), m)
    }

    @Test
    fun testDegenerateKeys() {
        val n = 100
        val sb = StringBuilder("\u5555\u5555")
        val hash = sb.toString().hashCode()
        val a = Array<String>(2 * n) {
            degenerateUpdate(sb)
            val s = sb.toString()
            assertEquals(hash, s.hashCode())
            s
        }
        val m = HashMap<String,String>()
        // add first batch
        for (i in 0..n - 1) {
            assertEquals(null, m.put(a[i], a[i]))
            assertEquals(a[i], m[a[i]])
        }
        // remove all even
        for (i in 0..n - 1) if (i % 2 == 0) {
            assertEquals(a[i], m.remove(a[i]))
        }
        // verify result
        for (i in 0..n - 1) {
            assertEquals(i % 2 != 0, m.contains(a[i]))
        }
        // add next batch
        for (i in n..2*n - 1) {
            assertEquals(null, m.put(a[i], a[i]))
            assertEquals(a[i], m[a[i]])
        }
        // remove all even
        for (i in n..2*n - 1) if (i % 2 == 0) {
            assertEquals(a[i], m.remove(a[i]))
        }
        // verify result
        for (i in 0..2*n - 1) {
            assertEquals(i % 2 != 0, m.contains(a[i]))
        }
        // readd even
        for (i in 0..2*n - 1) if (i % 2 == 0) {
            assertEquals(null, m.put(a[i], a[i]))
        }
        // verify ordering (all odd, then all even)
        val it = m.iterator()
        for (i in 0..2*n - 1) if (i % 2 != 0) {
            val entry = it.next()
            assertEquals(a[i], entry.key)
            assertEquals(a[i], entry.value)
        }
        for (i in 0..2*n - 1) if (i % 2 == 0) {
            val entry = it.next()
            assertEquals(a[i], entry.key)
            assertEquals(a[i], entry.value)
        }
        assertFalse(it.hasNext())
    }

    private fun degenerateUpdate(sb: java.lang.StringBuilder) {
        sb.setCharAt(0, (sb[0].toInt() - 1).toChar())
        sb.setCharAt(1, (sb[1].toInt() + 31).toChar())
    }
}