#include <windows.h>
#include "../main.h"
__declspec(dllexport) const char* pick_file() {
    static char file_name[MAX_PATH] = {0};

    // 创建文件选择对话框
    OPENFILENAME ofn;       // common dialog box structure
    HWND hwnd = NULL;       // owner window
    ZeroMemory(&ofn, sizeof(ofn));

    // 设置对话框参数
    ofn.lStructSize = sizeof(ofn);
    ofn.hwndOwner = hwnd;
    ofn.lpstrFile = file_name;
    ofn.lpstrFile[0] = '\0';
    ofn.nMaxFile = sizeof(file_name);
    ofn.lpstrFilter = "All\0";
    ofn.nFilterIndex = 1;
    ofn.lpstrFileTitle = NULL;
    ofn.nMaxFileTitle = 0;
    ofn.lpstrInitialDir = NULL;
    ofn.lpstrTitle = "Select a File";
    ofn.Flags = OFN_PATHMUSTEXIST | OFN_FILEMUSTEXIST;

    // 显示对话框
    if (GetOpenFileName(&ofn) == TRUE) {
        // 转换为UTF-8编码并返回
        int wchars_num = MultiByteToWideChar(CP_ACP, 0, file_name, -1, NULL, 0);
        if (wchars_num == 0) {
            return NULL;
        }

        wchar_t* wstr = (wchar_t*)malloc(wchars_num * sizeof(wchar_t));
        if (wstr == NULL) {
            return NULL;
        }

        MultiByteToWideChar(CP_ACP, 0, file_name, -1, wstr, wchars_num);

        // 将宽字符转换为UTF-8
        int utf8_chars_num = WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
        if (utf8_chars_num == 0) {
            free(wstr);
            return NULL;
        }

        char* utf8_str = (char*)malloc(utf8_chars_num);
        if (utf8_str == NULL) {
            free(wstr);
            return NULL;
        }

        WideCharToMultiByte(CP_UTF8, 0, wstr, -1, utf8_str, utf8_chars_num, NULL, NULL);
        free(wstr);

        return utf8_str; // 返回UTF-8编码的文件路径
    } else {
        return NULL;
    }
}

#include <stdlib.h>
#include <string.h>
#include <stdio.h>

__declspec(dllexport) const char* pick_files() {
    static char file_buffer[8192] = {0}; // 多选时需要较大的缓冲区
    static char* utf8_result = NULL;

    OPENFILENAME ofn;
    ZeroMemory(&ofn, sizeof(ofn));
    ofn.lStructSize = sizeof(ofn);
    ofn.hwndOwner = NULL;
    ofn.lpstrFile = file_buffer;
    ofn.lpstrFile[0] = '\0';
    ofn.nMaxFile = sizeof(file_buffer);
    ofn.lpstrFilter = "All Files\0*.*\0";
    ofn.nFilterIndex = 1;
    ofn.lpstrFileTitle = NULL;
    ofn.nMaxFileTitle = 0;
    ofn.lpstrInitialDir = NULL;
    ofn.lpstrTitle = "Select Files";
    ofn.Flags = OFN_PATHMUSTEXIST | OFN_FILEMUSTEXIST | OFN_ALLOWMULTISELECT;

    if (GetOpenFileName(&ofn) == TRUE) {
        // 清理之前的缓存
        if (utf8_result != NULL) {
            free(utf8_result);
            utf8_result = NULL;
        }

        char* p = file_buffer;
        char* dir = p;
        p += strlen(dir) + 1;

        if (*p == '\0') {
            // 只选择了一个文件
            dir = file_buffer;
            int wchars_num = MultiByteToWideChar(CP_ACP, 0, dir, -1, NULL, 0);
            wchar_t* wstr = (wchar_t*)malloc(wchars_num * sizeof(wchar_t));
            MultiByteToWideChar(CP_ACP, 0, dir, -1, wstr, wchars_num);

            int utf8_num = WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
            utf8_result = (char*)malloc(utf8_num);
            WideCharToMultiByte(CP_UTF8, 0, wstr, -1, utf8_result, utf8_num, NULL, NULL);
            free(wstr);

            return utf8_result;
        } else {
            // 多个文件，目录 + 多个文件名
            size_t total_len = 0;
            size_t count = 0;
            char* filenames[512]; // 最多支持512个文件

            while (*p) {
                filenames[count++] = p;
                total_len += strlen(dir) + 1 + strlen(p) + 1; // 路径 + `\` + 文件名 + `\n`
                p += strlen(p) + 1;
            }

            utf8_result = (char*)malloc(total_len * 4 + 1); // UTF-8更大一些
            if (!utf8_result) return NULL;
            utf8_result[0] = '\0';

            for (size_t i = 0; i < count; ++i) {
                char full_path[MAX_PATH];
                snprintf(full_path, sizeof(full_path), "%s\\%s", dir, filenames[i]);

                int wchars_num = MultiByteToWideChar(CP_ACP, 0, full_path, -1, NULL, 0);
                wchar_t* wstr = (wchar_t*)malloc(wchars_num * sizeof(wchar_t));
                MultiByteToWideChar(CP_ACP, 0, full_path, -1, wstr, wchars_num);

                int utf8_num = WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
                char* utf8_tmp = (char*)malloc(utf8_num);
                WideCharToMultiByte(CP_UTF8, 0, wstr, -1, utf8_tmp, utf8_num, NULL, NULL);

                strcat(utf8_result, utf8_tmp);
                strcat(utf8_result, "\n");

                free(wstr);
                free(utf8_tmp);
            }

            return utf8_result;
        }
    }

    return NULL;
}
