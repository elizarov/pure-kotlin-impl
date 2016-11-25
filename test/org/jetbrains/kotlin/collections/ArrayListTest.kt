package org.jetbrains.kotlin.collections

import org.junit.Assert.*
import org.junit.Test

class ArrayListTest {
    @Test
    fun testBasic() {
        val a = ArrayList<Int>()
        assertTrue(a.isEmpty())
        assertEquals(0, a.size)

        assertTrue(a.add(1))
        assertTrue(a.add(2))
        assertTrue(a.add(3))
        assertFalse(a.isEmpty())
        assertEquals(3, a.size)
        assertEquals(1, a[0])
        assertEquals(2, a[1])
        assertEquals(3, a[2])

        a[0] = 11
        assertEquals(11, a[0])

        assertEquals(11, a.removeAt(0))
        assertEquals(2, a.size)
        assertEquals(2, a[0])
        assertEquals(3, a[1])

        a.add(1, 22)
        assertEquals(3, a.size)
        assertEquals(2, a[0])
        assertEquals(22, a[1])
        assertEquals(3, a[2])

        a.clear()
        assertTrue(a.isEmpty())
        assertEquals(0, a.size)
    }

    @Test
    fun testIterator() {
        val a = ArrayList(listOf(1, 2, 3))
        val it = a.iterator()
        assertTrue(it.hasNext())
        assertEquals(1, it.next())
        assertTrue(it.hasNext())
        assertEquals(2, it.next())
        assertTrue(it.hasNext())
        assertEquals(3, it.next())
        assertFalse(it.hasNext())
    }

    @Test fun testRemove() {
        val a = ArrayList(listOf(1, 2, 3))
        assertTrue(a.remove(2))
        assertEquals(2, a.size)
        assertEquals(1, a[0])
        assertEquals(3, a[1])
        assertFalse(a.remove(2))
        assertEquals(2, a.size)
        assertEquals(1, a[0])
        assertEquals(3, a[1])
    }

    @Test fun testRemoveAll() {
        val a = ArrayList(listOf(1, 2, 3, 4, 5))
        assertFalse(a.removeAll(listOf(6, 7, 8)))
        assertEquals(listOf(1, 2, 3, 4, 5), a)
        assertTrue(a.removeAll(listOf(5, 3, 1)))
        assertEquals(listOf(2, 4), a)
    }

    @Test
    fun testRetainAll() {
        val a = ArrayList(listOf(1, 2, 3, 4, 5))
        assertFalse(a.retainAll(listOf(1, 2, 3, 4, 5)))
        assertEquals(listOf(1, 2, 3, 4, 5), a)
        assertTrue(a.retainAll(listOf(5, 3, 1)))
        assertEquals(listOf(1, 3, 5), a)
    }

    @Test
    fun testEquals() {
        val a = ArrayList(listOf(1, 2, 3))
        assertTrue(a == listOf(1, 2, 3))
        assertFalse(a == listOf(1, 2, 4))
        assertFalse(a == listOf(1, 2))
    }

    @Test
    fun testHashCode() {
        val a = ArrayList(listOf(1, 2, 3))
        assertTrue(a.hashCode() == listOf(1, 2, 3).hashCode())
    }

    @Test
    fun testToString() {
        val a = ArrayList(listOf(1, 2, 3))
        assertTrue(a.toString() == listOf(1, 2, 3).toString())
    }

    @Test
    fun testSubList() {
        val a0 = ArrayList(listOf(0, 1, 2, 3, 4))
        val a = a0.subList(1, 4)
        assertEquals(3, a.size)
        assertEquals(1, a[0])
        assertEquals(2, a[1])
        assertEquals(3, a[2])
        assertTrue(a == listOf(1, 2, 3))
        assertTrue(a.hashCode() == listOf(1, 2, 3).hashCode())
        assertTrue(a.toString() == listOf(1, 2, 3).toString())
    }

    @Test
    fun testResize() {
        val a = ArrayList<Int>()
        val n = 10000
        for (i in 1..n)
            assertTrue(a.add(i))
        assertEquals(n, a.size)
        for (i in 1..n)
            assertEquals(i, a[i - 1])
        a.trimToSize()
        assertEquals(n, a.size)
        for (i in 1..n)
            assertEquals(i, a[i - 1])
    }

    @Test
    fun testSubListContains() {
        val a = ArrayList(listOf(1, 2, 3, 4))
        val s = a.subList(1, 3)
        assertTrue(a.contains(1))
        assertFalse(s.contains(1))
        assertTrue(a.contains(2))
        assertTrue(s.contains(2))
        assertTrue(a.contains(3))
        assertTrue(s.contains(3))
        assertTrue(a.contains(4))
        assertFalse(s.contains(4))
    }

    @Test
    fun testSubListIndexOf() {
        val a = ArrayList(listOf(1, 2, 3, 4, 1))
        val s = a.subList(1, 3)
        assertEquals(0, a.indexOf(1))
        assertEquals(-1, s.indexOf(1))
        assertEquals(1, a.indexOf(2))
        assertEquals(0, s.indexOf(2))
        assertEquals(2, a.indexOf(3))
        assertEquals(1, s.indexOf(3))
        assertEquals(3, a.indexOf(4))
        assertEquals(-1, s.indexOf(4))
    }

    @Test
    fun testSubListLastIndexOf() {
        val a = ArrayList(listOf(1, 2, 3, 4, 1))
        val s = a.subList(1, 3)
        assertEquals(4, a.lastIndexOf(1))
        assertEquals(-1, s.lastIndexOf(1))
        assertEquals(1, a.lastIndexOf(2))
        assertEquals(0, s.lastIndexOf(2))
        assertEquals(2, a.lastIndexOf(3))
        assertEquals(1, s.lastIndexOf(3))
        assertEquals(3, a.lastIndexOf(4))
        assertEquals(-1, s.lastIndexOf(4))
    }

    @Test
    fun testSubListClear() {
        val a = ArrayList(listOf(1, 2, 3, 4))
        val s = a.subList(1, 3)
        assertEquals(listOf(2, 3), s)

        s.clear()
        assertEquals(listOf<Int>(), s)
        assertEquals(listOf(1, 4), a)
    }

    @Test
    fun testSubListAdd() {
        val a = ArrayList(listOf(1, 2, 3, 4))
        val s = a.subList(1, 3)
        assertEquals(listOf(2, 3), s)

        s.add(5)
        assertEquals(listOf(2, 3, 5), s)
        assertEquals(listOf(1, 2, 3, 5, 4), a)
    }

    @Test
    fun testSubListAddAll() {
        val a = ArrayList(listOf(1, 2, 3, 4))
        val s = a.subList(1, 3)
        assertEquals(listOf(2, 3), s)

        s.addAll(listOf(5, 6))
        assertEquals(listOf(2, 3, 5, 6), s)
        assertEquals(listOf(1, 2, 3, 5, 6, 4), a)
    }

    @Test
    fun testSubListRemoveAt() {
        val a = ArrayList(listOf(1, 2, 3, 4, 5))
        val s = a.subList(1, 4)
        assertEquals(listOf(2, 3, 4), s)

        s.removeAt(1)
        assertEquals(listOf(2, 4), s)
        assertEquals(listOf(1, 2, 4, 5), a)
    }

    @Test
    fun testSubListRemoveAll() {
        val a = ArrayList(listOf(1, 2, 3, 4, 5))
        val s = a.subList(1, 4)
        assertEquals(listOf(2, 3, 4), s)

        s.removeAll(listOf(3, 5))
        assertEquals(listOf(2, 4), s)
        assertEquals(listOf(1, 2, 4, 5), a)
    }

    @Test
    fun testSubListRetainAll() {
        val a = ArrayList(listOf(1, 2, 3, 4, 5))
        val s = a.subList(1, 4)
        assertEquals(listOf(2, 3, 4), s)

        s.retainAll(listOf(5, 3))
        assertEquals(listOf(3), s)
        assertEquals(listOf(1, 3, 5), a)
    }

    @Test
    fun testIteratorRemove() {
        val a = ArrayList(listOf(1, 2, 3, 4, 5))
        val it = a.iterator()
        while (it.hasNext())
            if (it.next() % 2 == 0)
                it.remove()
        assertEquals(listOf(1, 3, 5), a)
    }

    @Test
    fun testIteratorAdd() {
        val a = ArrayList(listOf(1, 2, 3, 4, 5))
        val it = a.listIterator()
        while (it.hasNext()) {
            val next = it.next()
            if (next % 2 == 0)
                it.add(-next)
        }
        assertEquals(listOf(1, 2, -2, 3, 4, -4, 5), a)
    }
}