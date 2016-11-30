package org.jetbrains.kotlin.collections

import collections.*
import collections.ArrayDeque
import org.junit.*
import org.junit.Assert.*
import java.util.*

class ArrayDequeTest {
    @Test
    fun addFirst() {
        val d = ArrayDeque<Int>(4)
        assertEquals(0, d.size)
        assertTrue(d.isEmpty())

        d.addFirst(1)
        assertEquals(1, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(1), d.toList())

        d.addFirst(2)
        assertEquals(2, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(2, 1), d.toList())

        d.addFirst(3)
        assertEquals(3, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(3, 2, 1), d.toList())

        d.addFirst(4)
        assertEquals(4, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(4, 3, 2, 1), d.toList())

        d.addFirst(5)
        assertEquals(5, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(5, 4, 3, 2, 1), d.toList())
    }

    @Test
    fun addLast() {
        val d = ArrayDeque<Int>(4)
        assertEquals(0, d.size)
        assertTrue(d.isEmpty())

        d.addLast(1)
        assertEquals(1, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(1), d.toList())

        d.addLast(2)
        assertEquals(2, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(1, 2), d.toList())

        d.addLast(3)
        assertEquals(3, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(1, 2, 3), d.toList())

        d.addLast(4)
        assertEquals(4, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(1, 2, 3, 4), d.toList())

        d.addLast(5)
        assertEquals(5, d.size)
        assertFalse(d.isEmpty())
        assertEquals(listOf(1, 2, 3, 4, 5), d.toList())
    }

    @Test
    fun addAllNoOverflow() {
        val d = ArrayDeque<Int>(4)
        d.addAll(listOf(1, 2, 3))
        assertEquals(3, d.size)
        assertEquals(listOf(1, 2, 3), d.toList())
    }

    @Test
    fun addAllWithOverflow() {
        val d = ArrayDeque<Int>(4)
        d.addAll(listOf(1, 2, 3, 4, 5))
        assertEquals(5, d.size)
        assertEquals(listOf(1, 2, 3, 4, 5), d.toList())
    }

    @Test
    fun removeFirst() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)

        assertEquals(4, d.size)
        assertEquals(listOf(1, 2, 3, 4), d.toList())
        assertEquals(1, d.peekFirst())
        assertEquals(4, d.peekLast())

        assertEquals(1, d.removeFirst())
        assertEquals(listOf(2, 3, 4), d.toList())
        assertEquals(3, d.size)
        assertEquals(2, d.peekFirst())
        assertEquals(4, d.peekLast())

        assertEquals(2, d.removeFirst())
        assertEquals(listOf(3, 4), d.toList())
        assertEquals(2, d.size)
        assertEquals(3, d.peekFirst())
        assertEquals(4, d.peekLast())

        assertEquals(3, d.removeFirst())
        assertEquals(listOf(4), d.toList())
        assertEquals(1, d.size)
        assertEquals(4, d.peekFirst())
        assertEquals(4, d.peekLast())

        assertEquals(4, d.removeFirst())
        assertEquals(0, d.size)
        assertEquals(listOf<Int>(), d.toList())
        assertTrue(d.isEmpty())
        assertEquals(null, d.peekFirst())
        assertEquals(null, d.peekLast())
        assertEquals(null, d.pollFirst())
        assertEquals(null, d.pollLast())
        assertEquals(null, d.poll())
    }

    @Test
    fun removeLast() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)

        assertEquals(4, d.size)
        assertEquals(listOf(1, 2, 3, 4), d.toList())
        assertEquals(1, d.peekFirst())
        assertEquals(4, d.peekLast())

        assertEquals(4, d.removeLast())
        assertEquals(listOf(1, 2, 3), d.toList())
        assertEquals(3, d.size)
        assertEquals(1, d.peekFirst())
        assertEquals(3, d.peekLast())

        assertEquals(3, d.removeLast())
        assertEquals(listOf(1, 2), d.toList())
        assertEquals(2, d.size)
        assertEquals(1, d.peekFirst())
        assertEquals(2, d.peekLast())

        assertEquals(2, d.removeLast())
        assertEquals(listOf(1), d.toList())
        assertEquals(1, d.size)
        assertEquals(1, d.peekFirst())
        assertEquals(1, d.peekLast())

        assertEquals(1, d.removeLast())
        assertEquals(0, d.size)
        assertEquals(listOf<Int>(), d.toList())
        assertTrue(d.isEmpty())
    }

    @Test
    fun removeElementInTheMiddle() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)

        d.remove(2)
        assertEquals(listOf(1, 3, 4), d.toList())
    }

    @Test
    fun removeElementAtStart() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)

        d.remove(1)
        assertEquals(listOf(2, 3, 4), d.toList())
    }

    @Test
    fun removeElementAtEnd() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)

        d.remove(4)
        assertEquals(listOf(1, 2, 3), d.toList())
    }

    @Test
    fun iteratorRead() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)

        assertEquals(listOf(1, 2, 3, 4), d.iterator().asSequence().toList())
    }

    @Test
    fun iteratorRemove() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)
        val it = d.iterator()

        while (it.hasNext()) {
            val v = it.next()
            if (v == 2 || v == 4) {
                it.remove()
            }
        }

        assertEquals(listOf(1, 3), d.toList())
    }

    @Test
    fun iteratorRemoveInShifted() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)
        d.removeLast()
        d.removeLast()
        d.addFirst(-1)
        d.addFirst(-2)

        assertEquals(listOf(-2, -1, 1, 2), d.toList())

        val it = d.iterator()

        while (it.hasNext()) {
            val v = it.next()
            if (v == -1 || v == 1) {
                it.remove()
            }
        }

        assertEquals(listOf(-2, 2), d.toList())
    }

    @Test
    fun growth() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)

        val steps = 100000
        for (i in 1..steps) {
            d.removeLast()

            d.addFirst(i)
            d.addFirst(i * 2)
        }

        assertEquals((steps downTo 1).flatMap { listOf(it * 2, it) }.take(steps + 4), d.toList())
    }

    @Test
    fun defaultCapacity() {
        val d = ArrayDeque<Int>()

        for (i in 1..10000) {
            d.add(i)
        }

        assertEquals((1..10000).toList(), d.toList())
    }

    @Test
    fun initialCapacity() {
        val d = ArrayDeque<Int>(17)

        for (i in 1..10000) {
            d.add(i)
        }

        assertEquals((1..10000).toList(), d.toList())
    }

    @Test
    fun removeAll() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)
        d.removeAll(listOf(2, 4))

        assertEquals(listOf(1, 3), d.toList())
    }

    @Test
    fun removeAllInShifted() {
        val d = ArrayDeque<Int>(4)
        d.addAll(1..4)
        d.removeLast()
        d.removeLast()
        d.addFirst(-1)
        d.addFirst(-2)


        assertEquals(listOf(-2, -1, 1, 2), d.toList())

        d.removeAll(listOf(1, -1))

        assertEquals(listOf(-2, 2), d.toList())
    }

    @Test
    fun pollFirst() {
        val d = ArrayDeque<Int>()
        d.addAll(1..4)

        assertEquals(listOf(1, 2, 3, 4, null), (1..5).map { d.pollFirst() })
    }

    @Test
    fun pollLast() {
        val d = ArrayDeque<Int>()
        d.addAll(1..4)

        assertEquals(listOf(4, 3, 2, 1, null), (1..5).map { d.pollLast() })
    }

    @Test
    fun pushAndPop() {
        val d = ArrayDeque<Int>()

        d.push(1)
        d.push(2)
        d.push(3)
        d.push(4)

        assertEquals(listOf(1, 2, 3, 4), (1..4).map { d.pop() })

        try {
            d.pop()
            fail()
        } catch (expected: NoSuchElementException) {
        }
    }
}