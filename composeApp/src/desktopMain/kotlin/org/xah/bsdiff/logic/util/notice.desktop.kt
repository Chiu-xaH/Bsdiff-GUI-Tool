package org.xah.bsdiff.logic.util

//import java.awt.AWTException
//import java.awt.SystemTray
//import java.awt.Toolkit
//import java.awt.TrayIcon


actual fun sendNotice(title: String, content: String?) {
    BsdiffJNI().warn()
//    if (!SystemTray.isSupported()) {
//        println("系统不支持通知")
//        return
//    }
//    val tray = SystemTray.getSystemTray()
//    val trayIcon = TrayIcon(Toolkit.getDefaultToolkit().createImage(""), "通知")
//    try {
//        tray.add(trayIcon)
//        trayIcon.displayMessage(title, content, TrayIcon.MessageType.INFO)
//    } catch (e: AWTException) {
//        e.printStackTrace()
//    }
}