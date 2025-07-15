package org.xah.bsdiff.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import bsdiffapp.composeapp.generated.resources.Res
import bsdiffapp.composeapp.generated.resources.arrow_split
import bsdiffapp.composeapp.generated.resources.folder_zip
import bsdiffapp.composeapp.generated.resources.`package`
import org.xah.bsdiff.ui.component.StyleCardListItem
import org.xah.bsdiff.ui.component.ResIcon
import org.xah.bsdiff.ui.util.NavRoute
import org.xah.bsdiff.ui.util.navigateAndSave

@Composable
fun HomeScreen(navHostController: NavHostController) {
    Column {
        StyleCardListItem(
            headlineContent = { Text("生成补丁包") },
            trailingContent = {
                ResIcon(Res.drawable.arrow_split)
            },
            modifier = Modifier.clickable {
                navHostController.navigateAndSave(NavRoute.SPLIT.name)
            }
        )
        StyleCardListItem(
            headlineContent = { Text("合并") },
            trailingContent = {
                ResIcon(Res.drawable.`package`)
            },
            modifier = Modifier.clickable {
                navHostController.navigateAndSave(NavRoute.MERGE.name)
            }
        )
        StyleCardListItem(
            headlineContent = { Text("MD5校验") },
            trailingContent = {
                ResIcon(Res.drawable.folder_zip)
            },
            modifier = Modifier.clickable {
                navHostController.navigateAndSave(NavRoute.CHECK.name)
            }
        )
    }
}
