package org.jetbrains.kotlin.text

import org.junit.Assert.assertEquals
import org.junit.Test

class StringBuilderTest {
    @Test
    fun testBasic() {
        val sb = StringBuilder()
        assertEquals(0, sb.length)
        assertEquals("", sb.toString())
        sb.append(1)
        assertEquals(1, sb.length)
        assertEquals("1", sb.toString())
        sb.append(", ")
        assertEquals(3, sb.length)
        assertEquals("1, ", sb.toString())
        sb.append(true)
        assertEquals(7, sb.length)
        assertEquals("1, true", sb.toString())
        sb.append(12345678L as Any)
        assertEquals(15, sb.length)
        assertEquals("1, true12345678", sb.toString())

        sb.length = 0
        assertEquals(0, sb.length)
        assertEquals("", sb.toString())
    }
}