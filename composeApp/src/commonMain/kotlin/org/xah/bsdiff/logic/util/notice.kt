package org.xah.bsdiff.logic.util

const val WINDOW_NAME = "增量更新工具"

fun sendNotice() {
    BsdiffJNI().warn(WINDOW_NAME)
}