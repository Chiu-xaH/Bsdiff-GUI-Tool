package org.xah.bsdiff.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ResIcon(id : DrawableResource, color : Color = LocalContentColor.current, modifier : Modifier = Modifier) {
    Icon(painterResource(id),null, tint = color, modifier = modifier)
}