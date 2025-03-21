package org.xah.bsdiff.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowCenter(modifier : Modifier = Modifier,content : @Composable () -> Unit) {
    Row (modifier = modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
        content()
    }
}