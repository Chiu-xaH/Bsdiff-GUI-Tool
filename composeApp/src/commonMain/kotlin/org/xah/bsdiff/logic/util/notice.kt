package org.xah.bsdiff.logic.util

import javax.swing.JOptionPane
import javax.swing.SwingUtilities

const val WINDOW_NAME = "增量更新工具"

fun sendNotice() {
    BsdiffJNI().warn(WINDOW_NAME)
}

fun showMsg(msg : String) {
    SwingUtilities.invokeLater { // 确保 UI 操作在主线程执行
        JOptionPane.showMessageDialog(null, msg)
    }
}