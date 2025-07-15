package org.xah.bsdiff.ui.util

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.draganddrop.DragAndDropTarget

actual fun onDrop(
    onDrag: (Boolean) -> Unit,
    onPath: (String?) -> Unit
): DragAndDropTarget {
    TODO("Not yet implemented")
}

actual fun onDrops(
    onDrag: (Boolean) -> Unit,
    onPaths: SnapshotStateList<String>
): DragAndDropTarget {
    TODO("Not yet implemented")
}