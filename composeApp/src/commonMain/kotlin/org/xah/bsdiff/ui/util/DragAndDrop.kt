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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

expect fun onDrop(
    onDrag : (Boolean) -> Unit,
    onPath : (String?) -> Unit
) : DragAndDropTarget

expect fun onDrops(
    onDrag : (Boolean) -> Unit,
    onPaths : SnapshotStateList<String>
) : DragAndDropTarget

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