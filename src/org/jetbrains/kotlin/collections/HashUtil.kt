package org.jetbrains.kotlin.collections

fun Any?.hashCode(): Int = if (this == null) 0 else hashCode()
