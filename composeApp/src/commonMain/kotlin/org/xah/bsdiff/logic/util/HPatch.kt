package org.xah.bsdiff.logic.util

object HPatch {
    private val hdiffz by lazy { extractExe("hdiffz.exe") }
    private val hpatchz by lazy { extractExe("hpatchz.exe") }

    fun patch(oldFilePath: String, newFilePath: String, patchFilePath: String) : Boolean {
        val cmd = listOf(
            hdiffz.absolutePath,
            "-f",
            oldFilePath,
            newFilePath,
            patchFilePath
        )
        val process = ProcessBuilder(cmd).start()
        return process.waitFor() == 0
    }

    fun merge(oldFilePath: String, patchFilePath: String, newFilePath: String) : Boolean {
        val cmd = listOf(
            hpatchz.absolutePath,
            "-f",
            oldFilePath,
            patchFilePath,
            newFilePath
        )
        val process = ProcessBuilder(cmd).start()
        return process.waitFor() == 0
    }
}

