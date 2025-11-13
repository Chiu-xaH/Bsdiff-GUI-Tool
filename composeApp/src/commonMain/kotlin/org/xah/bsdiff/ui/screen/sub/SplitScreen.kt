package org.xah.bsdiff.ui.screen.sub

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import bsdiffapp.composeapp.generated.resources.Res
import bsdiffapp.composeapp.generated.resources.delete
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.json.Json
import org.xah.bsdiff.logic.model.Patch
import org.xah.bsdiff.logic.model.PatchMeta
import org.xah.bsdiff.logic.util.addIfNotExists
import org.xah.bsdiff.logic.util.createPatch
import org.xah.bsdiff.logic.util.openFileExplorer
import org.xah.bsdiff.logic.util.pickFile
import org.xah.bsdiff.ui.component.BottomTip
import org.xah.bsdiff.ui.component.ResIcon
import org.xah.bsdiff.ui.component.RowCenter
import org.xah.bsdiff.ui.component.StyleCardListItem
import org.xah.bsdiff.ui.component.TransplantListItem
import org.xah.bsdiff.ui.screen.DoLoadingUI
import org.xah.bsdiff.ui.util.DropUI
import org.xah.bsdiff.ui.util.DropsUI
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


@Composable
fun SplitScreen() {
    var newFilePath by remember { mutableStateOf<String?>(null) }
    var patchFilePath by remember { mutableStateOf("") }
    var newFileName by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf<Boolean?>(null) }
    var successCount by remember { mutableStateOf<Int>(0) }
    var failCount by remember { mutableStateOf<Int>(0) }

    val scope = rememberCoroutineScope()
    val oldFilePath = remember { mutableStateListOf<String>() }

    LaunchedEffect(newFilePath,oldFilePath) {
        isSuccess = null
        newFilePath?.let { patchFilePath = getDefaultPath(it) }
        newFilePath?.let { newFileName = getFileName(it) }
    }

    if(!loading) {
        Box {
            Row(modifier = Modifier.padding(5.dp)) {
                DropUI("新文件", modifier = Modifier.fillMaxSize().weight(.5f)) {
                    newFilePath = it
                }
                Spacer(Modifier.width(5.dp))
                DropsUI("旧文件", modifier = Modifier.fillMaxSize().weight(.5f),oldFilePath)
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row {
                    TransplantListItem(
                        headlineContent = { Text("选择或拖入新文件") },
                        supportingContent = newFilePath?.let { { Text(it) } },
                        modifier = Modifier.weight(.5f).clickable {
                            scope.launch { newFilePath = pickFile() }
                        }
                    )
                    TransplantListItem(
                        headlineContent = { Text("添加或拖入若干旧文件") },
                        supportingContent = { Text(if(successCount + failCount == 0) "已添加 ${oldFilePath.size}" else "成功$successCount/失败$failCount/总${oldFilePath.size}") },
                        modifier = Modifier.weight(.5f).clickable {
                            scope.launch { pickFile()?.let { oldFilePath.addIfNotExists(it) } }
                        }
                    )
                }
                if(patchFilePath.isNotEmpty() && oldFilePath.isNotEmpty() && newFilePath != null) {
                    StyleCardListItem(
                        headlineContent = { patchFilePath.let{ Text(it, textDecoration = TextDecoration.Underline, modifier = Modifier.clickable { openFileExplorer(it) }) }},
                        supportingContent = { Text("生成.patch文件到目录") },
                        trailingContent = {
                            Button(
                                onClick = {
                                    scope.launch {
                                        loading = true
                                        val cpuCoreCount = getCpuCoreCount()
                                        val semaphore = Semaphore(cpuCoreCount) // 控制最大并发数
                                        val results = oldFilePath.map { oldPath ->
                                            async(Dispatchers.IO) {
                                                semaphore.withPermit {
//                                                    val patchFileName = getPatchFileName(newFileName, getFileName(oldPath))
//                                                    val result = createPatch(oldPath, newFilePath!!, applyPath(patchFilePath, patchFileName))
//                                                    if(result) {
//                                                        successCount++
//                                                    } else {
//                                                        failCount++
//                                                    }
                                                    try {
                                                        val oldFile = File(oldPath)
                                                        val newFile = File(newFilePath!!)

                                                        // 1. 生成 diff.bin
                                                        val diffFileName = "${getFileName(oldPath)}_to_${newFileName}.bin"
                                                        val diffFile = applyPath(patchFilePath, diffFileName)
                                                        val result = createPatch(oldPath, newFilePath!!, diffFile.absolutePath)
                                                        if (!result) {
                                                            failCount++
                                                            return@withPermit false
                                                        }

                                                        // 2. 计算 MD5
                                                        val oldMd5 = getMd5(oldFile)
                                                        val newMd5 = getMd5(newFile)

                                                        // 3. 构造 Patch 元数据
                                                        val patchMeta = Patch(
                                                            source = PatchMeta(md5 = oldMd5, versionCode = null, versionName = null),
                                                            target = PatchMeta(md5 = newMd5, versionCode = null, versionName = null)
                                                        )
                                                        val metaFile = File(diffFile.parentFile, "meta_${System.currentTimeMillis()}.json")
                                                        val jsonText = Json.encodeToString(Patch.serializer(), patchMeta)
                                                        metaFile.writeText(jsonText)

                                                        // 4. 打包成 .patch
                                                        val patchFileName = getPatchFileName(newFileName, getFileName(oldPath))
                                                        val patchFile = File(diffFile.parentFile, patchFileName)
                                                        ZipOutputStream(FileOutputStream(patchFile)).use { zip ->
                                                            zip.putNextEntry(ZipEntry("diff.bin"))
                                                            zip.write(diffFile.readBytes())
                                                            zip.closeEntry()

                                                            zip.putNextEntry(ZipEntry("meta.json"))
                                                            zip.write(metaFile.readBytes())
                                                            zip.closeEntry()
                                                        }

                                                        // 5. 清理临时文件
                                                        diffFile.delete()
                                                        metaFile.delete()

                                                        successCount++
                                                        true
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                        failCount++
                                                        false
                                                    }
                                                }
                                            }
                                        }

                                        // 等待所有任务完成
                                        val finalResults = results.awaitAll()
                                        isSuccess = true
                                        loading = false
                                    }
                                },
                                enabled = canStartPatches(newFilePath,oldFilePath),
                                shape = MaterialTheme.shapes.medium,
                            ) {
                                Text("生成补丁包")
                            }
                        }
                    )
                }
                BottomTip("${getCpuCoreCount()} Core Cpu")
                for (index in oldFilePath.indices) {
                    TransplantListItem(
                        headlineContent = oldFilePath[index].let { { Text(it) } },
                        modifier = Modifier.clickable {

                        },
                        leadingContent = { Text((index + 1).toString())},
                        trailingContent = {
                            IconButton(onClick = {
                                oldFilePath.removeAt(index)
                            }) {
                                ResIcon(Res.drawable.delete)
                            }
                        }
                    )
                }
                if(oldFilePath.isNotEmpty()) {
                    RowCenter {
                        Button (
                            onClick = {
                                scope.launch { pickFile()?.let { oldFilePath.addIfNotExists(it) } }
                            },
                            modifier = Modifier.padding(vertical = 5.dp)
                        ) {
                            Text("添加旧文件")
                        }
                        Spacer(Modifier.width(15.dp))
                        FilledTonalButton (
                            onClick = {
                                oldFilePath.sortDescending()
                            },
                            modifier = Modifier.padding(vertical = 5.dp)
                        ) {
                            Text("排序")
                        }
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.zIndex(2f).align(Alignment.TopCenter)) {
                RowCenter {
                    LinearProgressIndicator(
                        progress = { (successCount+failCount)/oldFilePath.size.toFloat() }
                    )
                }
                RowCenter {
                    Text("成功$successCount / 失败$failCount / 总${oldFilePath.size}")
                }
            }
        }
    }
    DoLoadingUI(patchFilePath, loading, isSuccess) {
        successCount = 0
        failCount = 0
        isSuccess = null
    }
}

fun canStartPatches(path1 : String?, path2: List<String>) : Boolean {
    // 未选择文件
    if(path1 == null || path2.isEmpty()) {
        return false
    }
    // 选择了同一个文件
    if(path1 in path2) {
        return false
    }
    // 扩展名必须一致
    path2.forEach {
        if(getFileExtension(path1) != getFileExtension(it)) {
            return false
        }
    }
    return true
}

fun canStartPatch(path1 : String?, path2: String?) : Boolean {
    // 未选择文件
    if(path1 == null || path2 == null) {
        return false
    }
    // 选择了同一个文件
    if(path1 == path2) {
        return false
    }
    // 扩展名必须一致
    if(getFileExtension(path1) != getFileExtension(path2)) {
        return false
    }
    return true
}

fun getMd5(file: File): String {
    val md = MessageDigest.getInstance("MD5")
    file.inputStream().use { input ->
        val buffer = ByteArray(8192)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            md.update(buffer, 0, bytesRead)
        }
    }
    return md.digest().joinToString("") { "%02x".format(it) }
}
fun getPatchFileName(newFileName : String,oldFileName : String) : String {
    // 补丁名为 旧文件_patched_新文件.新文件的扩展名
//    val e = getFileExtension(newFileName)
    val o = getFileNameWithoutExtension(oldFileName)
    val n = getFileNameWithoutExtension(newFileName)
    return o + "_to_" + n + "." + "patch"
}

// 在共享模块中定义expect函数
fun getDefaultPath(path: String): String {
    val file = File(path)
    return file.parent ?: ""  // 获取文件所在的目录
}
// 在共享模块中定义expect函数
fun getFileName(path: String): String {
    val file = File(path)
    return file.name  // 获取文件名（包括扩展名）
}

fun getFileExtension(fileName: String): String = fileName.substringAfterLast(".", "")

fun getFileNameWithoutExtension(fileName: String): String = fileName.substringBeforeLast(".") // 去掉扩展名
// 连接
fun applyPath(path: String, file: String): File = File(path,file)

fun getCpuCoreCount(): Int = Runtime.getRuntime().availableProcessors()