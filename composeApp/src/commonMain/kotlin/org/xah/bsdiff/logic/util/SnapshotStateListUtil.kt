package org.xah.bsdiff.logic.util

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.addIfNotExists(item: T) {
    if (!contains(item)) add(item)
}