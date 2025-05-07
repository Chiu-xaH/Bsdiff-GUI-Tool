#include "jni.h"
#include "../main.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <windows.h>
#include "jni_native.h"

JNIEXPORT jstring JNICALL Java_org_xah_bsdiff_logic_util_BsdiffJNI_pickFiles
  (JNIEnv * env, jobject obj) {
    // 调用C函数 pick_file
    const char* filePath = pick_files();

    // 如果没有文件被选择，返回null
    if (filePath == NULL) {
        return NULL;
    }

    // 将C的const char* 转换为jstring并返回
    return (*env)->NewStringUTF(env, filePath);
}


JNIEXPORT jstring JNICALL Java_org_xah_bsdiff_logic_util_BsdiffJNI_pickFile
  (JNIEnv * env, jobject obj) {
    // 调用C函数 pick_file
    const char* filePath = pick_file();

    // 如果没有文件被选择，返回null
    if (filePath == NULL) {
        return NULL;
    }

    // 将C的const char* 转换为jstring并返回
    return (*env)->NewStringUTF(env, filePath);
}

JNIEXPORT jboolean JNICALL Java_org_xah_bsdiff_logic_util_BsdiffJNI_warn
  (JNIEnv *env, jobject obj, jstring windowName) {

    const jchar* windowNameChars = (*env)->GetStringChars(env, windowName, NULL);
    jsize length = (*env)->GetStringLength(env, windowName);
    wchar_t* windowNameW = (wchar_t*)malloc((length + 1) * sizeof(wchar_t)); // 为宽字符数组分配空间

    wcsncpy(windowNameW, (const wchar_t*)windowNameChars, length);
    windowNameW[length] = L'\0';  // 确保以 NULL 结尾

    bool result = warn(windowNameW);

    (*env)->ReleaseStringChars(env, windowName, windowNameChars);
    free(windowNameW);

    return result ? JNI_TRUE : JNI_FALSE;
}

#ifdef _WIN32
#endif

JNIEXPORT jboolean JNICALL Java_org_xah_bsdiff_logic_util_BsdiffJNI_merge
  (JNIEnv *env, jobject obj, jstring oldFilePath, jstring patchFilePath, jstring newFilePath) {

    // Windows 平台需要先获取 UTF-16 编码
#ifdef _WIN32
    const jchar* oldFile_w = (*env)->GetStringChars(env, oldFilePath, NULL);
    const jchar* newFile_w = (*env)->GetStringChars(env, newFilePath, NULL);
    const jchar* patchFile_w = (*env)->GetStringChars(env, patchFilePath, NULL);

    int oldLen = (*env)->GetStringLength(env, oldFilePath);
    int newLen = (*env)->GetStringLength(env, newFilePath);
    int patchLen = (*env)->GetStringLength(env, patchFilePath);

    // 计算转换后的字符串长度
    int oldFileSize = WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)oldFile_w, oldLen, NULL, 0, NULL, NULL);
    int newFileSize = WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)newFile_w, newLen, NULL, 0, NULL, NULL);
    int patchFileSize = WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)patchFile_w, patchLen, NULL, 0, NULL, NULL);

    char* oldFile = (char*)malloc(oldFileSize + 1);
    char* newFile = (char*)malloc(newFileSize + 1);
    char* patchFile = (char*)malloc(patchFileSize + 1);

    WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)oldFile_w, oldLen, oldFile, oldFileSize, NULL, NULL);
    WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)newFile_w, newLen, newFile, newFileSize, NULL, NULL);
    WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)patchFile_w, patchLen, patchFile, patchFileSize, NULL, NULL);

    oldFile[oldFileSize] = '\0';
    newFile[newFileSize] = '\0';
    patchFile[patchFileSize] = '\0';

    (*env)->ReleaseStringChars(env, oldFilePath, oldFile_w);
    (*env)->ReleaseStringChars(env, newFilePath, newFile_w);
    (*env)->ReleaseStringChars(env, patchFilePath, patchFile_w);

#else
    // Linux/macOS 直接使用 UTF-8
    const char* oldFile = (*env)->GetStringUTFChars(env, oldFilePath, NULL);
    const char* newFile = (*env)->GetStringUTFChars(env, newFilePath, NULL);
    const char* patchFile = (*env)->GetStringUTFChars(env, patchFilePath, NULL);
#endif

    // 调用合并函数
    bool result = merge(oldFile, patchFile, newFile);

#ifdef _WIN32
    free(oldFile);
    free(newFile);
    free(patchFile);
#else
    // Linux/macOS 释放 UTF-8 资源
    (*env)->ReleaseStringUTFChars(env, oldFilePath, oldFile);
    (*env)->ReleaseStringUTFChars(env, newFilePath, newFile);
    (*env)->ReleaseStringUTFChars(env, patchFilePath, patchFile);
#endif
    return result ? JNI_TRUE : JNI_FALSE;
}


#ifdef _WIN32
#endif

JNIEXPORT jboolean JNICALL Java_org_xah_bsdiff_logic_util_BsdiffJNI_patch
        (JNIEnv *env, jobject obj, jstring oldFilePath, jstring newFilePath, jstring patchFilePath) {

#ifdef _WIN32
    // Windows: 获取 UTF-16 编码的 jstring
    const jchar* oldFile_w = (*env)->GetStringChars(env, oldFilePath, NULL);
    const jchar* newFile_w = (*env)->GetStringChars(env, newFilePath, NULL);
    const jchar* patchFile_w = (*env)->GetStringChars(env, patchFilePath, NULL);

    int oldLen = (*env)->GetStringLength(env, oldFilePath);
    int newLen = (*env)->GetStringLength(env, newFilePath);
    int patchLen = (*env)->GetStringLength(env, patchFilePath);

    // 计算转换后的字符串长度
    int oldFileSize = WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)oldFile_w, oldLen, NULL, 0, NULL, NULL);
    int newFileSize = WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)newFile_w, newLen, NULL, 0, NULL, NULL);
    int patchFileSize = WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)patchFile_w, patchLen, NULL, 0, NULL, NULL);

    char* oldFile = (char*)malloc(oldFileSize + 1);
    char* newFile = (char*)malloc(newFileSize + 1);
    char* patchFile = (char*)malloc(patchFileSize + 1);

    WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)oldFile_w, oldLen, oldFile, oldFileSize, NULL, NULL);
    WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)newFile_w, newLen, newFile, newFileSize, NULL, NULL);
    WideCharToMultiByte(CP_ACP, 0, (LPCWSTR)patchFile_w, patchLen, patchFile, patchFileSize, NULL, NULL);

    oldFile[oldFileSize] = '\0';
    newFile[newFileSize] = '\0';
    patchFile[patchFileSize] = '\0';

    (*env)->ReleaseStringChars(env, oldFilePath, oldFile_w);
    (*env)->ReleaseStringChars(env, newFilePath, newFile_w);
    (*env)->ReleaseStringChars(env, patchFilePath, patchFile_w);

#else
    // Linux/macOS 直接获取 UTF-8
    const char* oldFile = (*env)->GetStringUTFChars(env, oldFilePath, NULL);
    const char* newFile = (*env)->GetStringUTFChars(env, newFilePath, NULL);
    const char* patchFile = (*env)->GetStringUTFChars(env, patchFilePath, NULL);
#endif

    // 调用补丁函数
    bool result = patch(oldFile, newFile, patchFile);

#ifdef _WIN32
    free(oldFile);
    free(newFile);
    free(patchFile);
#else
    // Linux/macOS 释放 UTF-8 资源
    (*env)->ReleaseStringUTFChars(env, oldFilePath, oldFile);
    (*env)->ReleaseStringUTFChars(env, newFilePath, newFile);
    (*env)->ReleaseStringUTFChars(env, patchFilePath, patchFile);
#endif
    return result ? JNI_TRUE : JNI_FALSE;
}