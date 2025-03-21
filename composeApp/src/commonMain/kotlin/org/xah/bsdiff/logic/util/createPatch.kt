package org.xah.bsdiff.logic.util


expect suspend fun createPatch(
    oldFilePath: String,
    newFilePath: String,
    patchFilePath: String,
    callback: (Boolean) -> Unit) : Boolean


expect suspend fun mergePatch(
    oldFilePath: String,
    patchFilePath: String,
    newFilePath: String,
    callback: (Boolean) -> Unit
) : Boolean