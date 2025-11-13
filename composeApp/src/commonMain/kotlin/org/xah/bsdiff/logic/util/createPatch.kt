package org.xah.bsdiff.logic.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun createPatch(
    oldFilePath: String,
    newFilePath: String,
    patchFilePath: String,
)  : Boolean  {
    return withContext(Dispatchers.IO) {
        val jni = BsdiffJNI()
        // 调用 JNI 函数并返回结果
        jni.patch(oldFilePath, newFilePath, patchFilePath)
    }
}
suspend fun mergePatch(
    oldFilePath: String,
    patchFilePath: String,
    newFilePath: String,
): Boolean {
    return withContext(Dispatchers.IO) {
        val jni = BsdiffJNI()
        jni.merge(oldFilePath,patchFilePath, newFilePath)
    }
}