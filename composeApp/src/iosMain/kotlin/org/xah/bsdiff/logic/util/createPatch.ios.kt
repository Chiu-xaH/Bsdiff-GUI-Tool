package org.xah.bsdiff.logic.util

actual suspend fun createPatch(
    oldFilePath: String,
    newFilePath: String,
    patchFilePath: String,
    callback: (Boolean) -> Unit
) : Boolean {
    TODO("Not yet implemented")
}

actual suspend fun mergePatch(
    oldFilePath: String,
    patchFilePath: String,
    newFilePath: String,
    callback: (Boolean) -> Unit
): Boolean {
    TODO("Not yet implemented")
}