package org.xah.bsdiff.ui.screen.sub

import java.io.File


actual fun getDefaultPath(path: String): String {
    val file = File(path)
    return file.parent ?: ""  // 获取文件所在的目录
}

actual fun getFileName(path: String): String {
    val file = File(path)
    return file.name  // 获取文件名（包括扩展名）
}

actual fun applyPath(path: String, file: String): String = File(path,file).absolutePath