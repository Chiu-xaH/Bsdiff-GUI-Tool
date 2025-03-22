package org.xah.bsdiff.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bsdiffapp.composeapp.generated.resources.Res
import bsdiffapp.composeapp.generated.resources.arrow_back
import org.xah.bsdiff.ui.component.BottomTip
import org.xah.bsdiff.ui.component.ResIcon
import org.xah.bsdiff.ui.component.appHorizontalDp
import org.xah.bsdiff.ui.screen.CheckScreen
import org.xah.bsdiff.ui.screen.HomeScreen
import org.xah.bsdiff.ui.screen.MergeScreen
import org.xah.bsdiff.ui.screen.SplitScreen
import org.xah.bsdiff.ui.util.AnimationManager.fadeAnimation
import org.xah.bsdiff.ui.util.NavRoute
import org.xah.bsdiff.ui.util.isCurrentRoute
import org.xah.bsdiff.ui.util.navigateAndClear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navHostController = rememberNavController()
    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Column {
                    LargeTopAppBar(
                        title = {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "增量更新工具 1.0.1",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color.Transparent),
                        actions = {
                            // 如果已经在首页，则不显示
                            if (!navHostController.isCurrentRoute(NavRoute.HOME.name)) {
                                IconButton(
                                    onClick = {
                                        navHostController.navigateAndClear(NavRoute.HOME.name) // 回到上一级
                                    }
                                ) {
                                    ResIcon(Res.drawable.arrow_back)
                                }
                            }
                        },
                        navigationIcon = {
                            Column(modifier = Modifier
                                .padding(horizontal = 23.dp)) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "基于Bsdiff算法",
                                    fontSize = 38.sp,
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                )
                            }
                        }
                    )
                    BottomTip("不要使用中文目录 目前会崩溃")
                    Spacer(Modifier.height(10.dp))
                }
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navHostController,
                startDestination = NavRoute.HOME.name,
                enterTransition = {
                    fadeAnimation.enter
                },
                exitTransition = {
                    fadeAnimation.exit
                }
            ) {
                composable(NavRoute.HOME.name) {
                    HomeScreen(navHostController)
                }
                composable(NavRoute.CHECK.name) {
                    CheckScreen()
                }
                composable(NavRoute.MERGE.name) {
                    MergeScreen()
                }
                composable(NavRoute.SPLIT.name) {
                    SplitScreen()
                }
            }
        }
    }
}








