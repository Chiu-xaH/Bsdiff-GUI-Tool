package org.xah.bsdiff

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.xah.bsdiff.ui.screen.App

const val WINDOW_NAME = "增量更新工具"

fun main() = application {
    // 创建初始的 WindowState
    val windowState = rememberWindowState(
        width = 580.dp,
        height = 440.dp,
        position = WindowPosition(Alignment.Center) // 设置居中位置
    )
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = WINDOW_NAME,
    ) {
        App()
    }
}