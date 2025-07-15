package org.xah.bsdiff.ui.util

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import org.xah.bsdiff.logic.util.addIfNotExists
import java.io.File
import java.net.URI


@OptIn(ExperimentalComposeUiApi::class)
actual fun onDrop(
    onDrag : (Boolean) -> Unit,
    onPath : (String?) -> Unit
) = object: DragAndDropTarget {
    override fun onStarted(event: DragAndDropEvent) = onDrag(true)

    override fun onEnded(event: DragAndDropEvent) = onDrag(false)

    override fun onDrop(event: DragAndDropEvent): Boolean {
        // 处理拖入的文件 得到文件路径回调给onPath
        val dragData = event.dragData()
        if (dragData is DragData.FilesList) {
            val fileUri = dragData.readFiles().firstOrNull()
            val absolutePath = try {
                fileUri?.let { URI(it) }?.let { File(it).absolutePath }
            } catch (e: Exception) {
                null
            }
            onPath(absolutePath)
            return true
        } else {
            onPath(null)
            return false
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
actual fun onDrops(
    onDrag: (Boolean) -> Unit,
    onPaths: SnapshotStateList<String>
) = object: DragAndDropTarget{
    override fun onStarted(event: DragAndDropEvent) = onDrag(true)

    override fun onEnded(event: DragAndDropEvent) = onDrag(false)

    override fun onDrop(event: DragAndDropEvent): Boolean {
        // 处理拖入的文件 得到文件路径回调给onPath
        val dragData = event.dragData()
        if (dragData is DragData.FilesList) {
            val fileUri = dragData.readFiles()
            fileUri.forEach { item ->
                val absolutePath = try {
                    File(URI(item)).absolutePath
                } catch (e: Exception) {
                    null
                }
                absolutePath?.let { item -> onPaths.addIfNotExists(item) }
            }

            return true
        } else {
            return false
        }
    }
}