package org.jetbrains.kotlin.collections

import org.junit.Assert.*
import org.junit.Test

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

        assertEquals("a", m.remove("1"))
        assertFalse(m.containsKey("1"))
        assertFalse(m.containsValue("a"))
        assertEquals(null, m.get("1"))
        assertTrue(m.containsKey("2"))
        assertTrue(m.containsValue("b"))
        assertEquals("b", m.get("2"))
        assertFalse(m.isEmpty())
        assertEquals(1, m.size)

        assertEquals("b", m.remove("2"))
        assertFalse(m.containsKey("1"))
        assertFalse(m.containsValue("a"))
        assertEquals(null, m.get("1"))
        assertFalse(m.containsKey("2"))
        assertFalse(m.containsValue("b"))
        assertEquals(null, m.get("2"))
        assertTrue(m.isEmpty())
        assertEquals(0, m.size)
    }

    @Test
    fun testRehash() {
        val m = HashMap<String, String>()
        val n = 10000
        for (i in 1..n) {
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