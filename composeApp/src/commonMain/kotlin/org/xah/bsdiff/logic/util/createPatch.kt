package org.xah.bsdiff.logic.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun createPatch(
    oldFilePath: String,
    newFilePath: String,
    patchFilePath: String,
    useHPatch : Boolean,
)  : Boolean  {
    return withContext(Dispatchers.IO) {
        if(useHPatch) {
            HPatch.patch(oldFilePath, newFilePath, patchFilePath)
        } else {
            val jni = BsdiffJNI()
            // 调用 JNI 函数并返回结果
            jni.patch(oldFilePath, newFilePath, patchFilePath)
        }
    }
}

suspend fun mergePatch(
    oldFilePath: String,
    patchFilePath: String,
    newFilePath: String,
    useHPatch : Boolean,
): Boolean {
    return withContext(Dispatchers.IO) {
        if(useHPatch) {
            HPatch.merge(oldFilePath,patchFilePath, newFilePath)
        } else {
            val jni = BsdiffJNI()
            jni.merge(oldFilePath,patchFilePath, newFilePath)
        }
    }
}