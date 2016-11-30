package org.jetbrains.kotlin.collections

import java.util.*

private val emptyArray = emptyArray<Any?>()

/**
 * Returns an array of objects of the given type with the given [size] with _uninitialized_ values.
 * Attempts to read _uninitialized_ values from this array work in implementation-dependent manner,
 * either throwing exception or returning some value.
 */
fun intArrayOfUninitializedElements(size: Int): IntArray {
    // note: Kotlin Native does not have to zero-out allocated memory here
    return IntArray(size)
}

/**
 * Returns new array which is a copy of the original array with new elements filled with _uninitialized_ values.
 * Attempts to read _uninitialized_ values from this array work in implementation-dependent manner,
 * either throwing exception or returning some value.
 */
fun IntArray.copyOfUninitializedElements(newSize: Int): IntArray {
    // note: Kotlin Native does not have to zero-out allocated memory here
    return copyOf(newSize)
}

/**
 * Returns an array of objects of the given type with the given [size] with _uninitialized_ values.
 * Attempts to read _uninitialized_ values from this array work in implementation-dependent manner,
 * either throwing exception or returning some value.
 */
@Suppress("UNCHECKED_CAST")
fun <E> arrayOfUninitializedElements(size: Int): Array<E> {
    return (if (size == 0) emptyArray else arrayOfNulls<Any>(size)) as Array<E>
}

/**
 * Returns new array which is a copy of the original array with new elements filled with _uninitialized_ values.
 * Attempts to read _uninitialized_ values from this array work in implementation-dependent manner,
 * either throwing exception or returning some value.
 */
@Suppress("UNCHECKED_CAST")
fun <E> Array<E>.copyOfUninitializedElements(newSize: Int): Array<E> {
    return copyOf(newSize) as Array<E>
}

/**
 * Resets an array element at a specified index to some implementation-specific _uninitialized_ value.
 * In particular, references stored in this element are released and become available for garbage collection.
 * Attempts to read _uninitialized_ value work in implementation-dependent manner,
 * either throwing exception or returning some value.
 */
@Suppress("UNCHECKED_CAST")
fun <E> Array<E>.resetAt(index: Int) {
    (this as Array<Any?>)[index] = null
}

/**
 * Resets a range of array elements at a specified [fromIndex] (inclusive) to [toIndex] (exclusive) range of indices
 * to some implementation-specific _uninitialized_ value.
 * In particular, references stored in these elements are released and become available for garbage collection.
 * Attempts to read _uninitialized_ values work in implementation-dependent manner,
 * either throwing exception or returning some value.
 */
@Suppress("UNCHECKED_CAST")
fun <E> Array<E>.resetRange(fromIndex: Int, toIndex: Int) {
    Arrays.fill(this, fromIndex, toIndex, null)
}

/**
 * Copies a range of array elements at a specified [fromIndex] (inclusive) to [toIndex] (exclusive) range of indices
 * to another [destination] array starting at [destinationIndex].
 */
fun <E> Array<E>.copyRangeTo(destination: Array<E>, fromIndex: Int, toIndex: Int, destinationIndex: Int = 0) {
    System.arraycopy(this, fromIndex, destination, destinationIndex, toIndex - fromIndex)
}

/**
 * Copies a range of array elements at a specified [fromIndex] (inclusive) to [toIndex] (exclusive) range of indices
 * to another part of this array starting at [destinationIndex].
 */
fun <E> Array<E>.copyRange(fromIndex: Int, toIndex: Int, destinationIndex: Int = 0) {
    copyRangeTo(this, fromIndex, toIndex, destinationIndex)
}
