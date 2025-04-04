package org.xah.bsdiff.logic.util

class BsdiffJNI {
    companion object {
        init {
            System.loadLibrary("bsdiff")
        }
    }

    external fun patch(oldFilePath: String, newFilePath: String, patchFilePath: String) : Boolean

    external fun merge(oldFilePath: String, patchFilePath: String, newFilePath: String) : Boolean

    external fun pickFile() : String?

    external fun warn(windowName : String) : Boolean
}