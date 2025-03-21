package org.xah.bsdiff.logic.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual suspend fun createPatch(
    oldFilePath: String,
    newFilePath: String,
    patchFilePath: String,
    callback: (Boolean) -> Unit
)  : Boolean {
    // 加载
    callback(true)
    // 启动一个协程进行修补操作
    val result = withContext(Dispatchers.IO) {
        val jni = BsdiffJNI()
        jni.patch(oldFilePath, newFilePath, patchFilePath)
    }

    // 调用回调函数
    callback(false)
    return result == 0
}

actual suspend fun mergePatch(
    oldFilePath: String,
    patchFilePath: String,
    newFilePath: String,
    callback: (Boolean) -> Unit
): Boolean {
    // 加载
    callback(true)
    // 启动一个协程进行修补操作
//    println(oldFilePath)
//    println(patchFilePath)
//    println(newFilePath)
    val result = withContext(Dispatchers.IO) {
        val jni = BsdiffJNI()
        jni.merge(oldFilePath,patchFilePath, newFilePath)
    }
//    println(result)
    callback(false)
    return result == 1
}