package org.xah.bsdiff.ui.util

import androidx.navigation.NavController

fun NavController.navigateAndClear(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { inclusive = true } // 清除所有历史记录
        launchSingleTop = true // 避免多次实例化相同的目的地
    }
}

fun NavController.navigateAndSave(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.isCurrentRoute(route : String) : Boolean = this.currentBackStackEntry?.destination?.route == route