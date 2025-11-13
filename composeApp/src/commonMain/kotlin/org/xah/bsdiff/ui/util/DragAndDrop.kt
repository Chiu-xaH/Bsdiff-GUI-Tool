package org.xah.bsdiff.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.xah.bsdiff.logic.util.addIfNotExists
import java.io.File
import java.net.URI

@OptIn(ExperimentalComposeUiApi::class)
fun onDrop(
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
fun onDrops(
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

@Composable
fun DropUI(tipText : String,modifier: Modifier = Modifier,onPath: (String?) -> Unit) {
    var isDragging by remember { mutableStateOf(false) }
    val dragAndDropTarget = remember {
        onDrop(
            onDrag = { isDragging = it },
            onPath = onPath
        )
    }
    Box(modifier = modifier.background(Color.Transparent)
        .dragAndDropTarget(
            shouldStartDragAndDrop = { true },
            target = dragAndDropTarget
        )
    ) {
        if(isDragging) {
            Box(modifier = Modifier
                .fillMaxSize()
                .border(
                    width = if(isDragging)2.dp else 0.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )
                .background(MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.medium)
            ) {
                Text(tipText, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun DropsUI(tipText : String,modifier: Modifier = Modifier,onPaths : SnapshotStateList<String>) {
    var isDragging by remember { mutableStateOf(false) }
    val dragAndDropTarget = remember {
        onDrops(
            onDrag = { isDragging = it },
            onPaths = onPaths
        )
    }
    Box(modifier = modifier.background(Color.Transparent)
        .dragAndDropTarget(
            shouldStartDragAndDrop = { true },
            target = dragAndDropTarget
        )
    ) {
        if(isDragging) {
            Box(modifier = Modifier
                .fillMaxSize()
                .border(
                    width = if(isDragging)2.dp else 0.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )
                .background(MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.medium)
            ) {
                Text(tipText, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}