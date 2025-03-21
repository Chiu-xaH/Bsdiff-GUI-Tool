package org.xah.bsdiff.ui.screen

import java.io.File
import java.security.MessageDigest

actual fun isSimpleFile(path1: String, path2: String, callback: (Boolean) -> Unit): Boolean {
    callback(true)
    val result = File(path1).md5() == File(path2).md5()
    callback(false)
    return result
}

private fun File.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    inputStream().use { fis ->
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (fis.read(buffer).also { bytesRead = it } != -1) {
            md.update(buffer, 0, bytesRead)
        }
    }
    return md.digest().joinToString("") { "%02x".format(it) }
}