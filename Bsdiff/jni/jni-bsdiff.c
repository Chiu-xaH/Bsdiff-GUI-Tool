#include "jni.h"
#include "../main.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iconv.h>
#include "jni_native.h"

JNIEXPORT int JNICALL Java_org_xah_bsdiff_logic_util_BsdiffJNI_patch
  (JNIEnv *env, jobject obj, jstring oldFilePath, jstring newFilePath, jstring patchFilePath) {

    // 将Java的jstring转换为C字符串
    const char* oldFile = (*env)->GetStringUTFChars(env, oldFilePath, NULL);
    const char* newFile = (*env)->GetStringUTFChars(env, newFilePath, NULL);
    const char* patchFile = (*env)->GetStringUTFChars(env, patchFilePath, NULL);

    int result = patch(oldFile,newFile,patchFile);

    // 释放资源
    (*env)->ReleaseStringUTFChars(env, oldFilePath, oldFile);
    (*env)->ReleaseStringUTFChars(env, newFilePath, newFile);
    (*env)->ReleaseStringUTFChars(env, patchFilePath, patchFile);

    return result;
}


JNIEXPORT int JNICALL Java_org_xah_bsdiff_logic_util_BsdiffJNI_merge
  (JNIEnv *env, jobject obj, jstring oldFilePath, jstring patchFilePath, jstring newFilePath) {

    // 将Java的jstring转换为C字符串
    const char* oldFile = (*env)->GetStringUTFChars(env, oldFilePath, NULL);
    const char* newFile = (*env)->GetStringUTFChars(env, newFilePath, NULL);
    const char* patchFile = (*env)->GetStringUTFChars(env, patchFilePath, NULL);

    int result = merge(oldFile,patchFile,newFile);

    // 释放资源
    (*env)->ReleaseStringUTFChars(env, oldFilePath, oldFile);
    (*env)->ReleaseStringUTFChars(env, newFilePath, newFile);
    (*env)->ReleaseStringUTFChars(env, patchFilePath, patchFile);

    return result;
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