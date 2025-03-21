package org.xah.bsdiff.logic.util

import android.net.Uri
import android.provider.OpenableColumns
import org.xah.bsdiff.App
import java.io.File
import java.io.IOException


actual suspend fun pickFile(): String? {
    return null
}


/**获取文件路径*/
fun getFilePathFromUri(uri: Uri): String? {
    App.context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex != -1 && cursor.moveToFirst()) {
            return cursor.getString(nameIndex)
        }
    }
    return uri.path // 如果无法获取，则返回 URI 路径
}

actual fun openFileExplorer(path : String) {

}