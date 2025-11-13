package org.xah.bsdiff.logic.model

import java.io.File

data class PatchContent(
    val meta: Patch,
    val diffFile: File
)