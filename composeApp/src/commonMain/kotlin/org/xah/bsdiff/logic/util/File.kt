package org.xah.bsdiff.logic.util

import java.awt.Desktop
import java.io.File
import java.io.IOException


fun pickFile(): String? = BsdiffJNI().pickFile()

fun openFileExplorer(path : String) {
    if (Desktop.isDesktopSupported()) {
        try {
            val desktop = Desktop.getDesktop()
            val file = File(path)

            // 判断路径是否存在且是一个目录
            if (file.exists() && file.isDirectory) {
                desktop.open(file)  // 打开文件资源管理器
            } else {
                println("路径不存在或不是一个文件夹！")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    } else {
        println("不支持桌面操作！")
    }
}

//expect suspend fun pickFiles() : String?
