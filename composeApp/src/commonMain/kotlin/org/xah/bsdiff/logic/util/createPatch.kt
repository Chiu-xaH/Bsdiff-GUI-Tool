package org.xah.bsdiff.logic.util


expect suspend fun createPatch(
    oldFilePath: String,
    newFilePath: String,
    patchFilePath: String
) : Boolean


expect suspend fun mergePatch(
    oldFilePath: String,
    patchFilePath: String,
    newFilePath: String
) : Boolean