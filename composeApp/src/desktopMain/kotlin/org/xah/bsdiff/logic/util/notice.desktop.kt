package org.xah.bsdiff.logic.util

import org.xah.bsdiff.WINDOW_NAME


actual fun sendNotice() {
    BsdiffJNI().warn(WINDOW_NAME)
}