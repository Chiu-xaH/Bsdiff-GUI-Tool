#include <windows.h>
#include <wchar.h>

// 返回宽字符路径
//__declspec(dllexport) const char* pick_file() {
//    static char file_name[MAX_PATH] = {0};
//
//    // 创建文件选择对话框
//    OPENFILENAME ofn;       // common dialog box structure
//    HWND hwnd = NULL;       // owner window
//    ZeroMemory(&ofn, sizeof(ofn));
//
//    // 设置对话框参数
//    ofn.lStructSize = sizeof(ofn);
//    ofn.hwndOwner = hwnd;
//    ofn.lpstrFile = file_name;
//    ofn.lpstrFile[0] = '\0';
//    ofn.nMaxFile = sizeof(file_name);
//    ofn.lpstrFilter = "All\0";
//    ofn.nFilterIndex = 1;
//    ofn.lpstrFileTitle = NULL;
//    ofn.nMaxFileTitle = 0;
//    ofn.lpstrInitialDir = NULL;
//    ofn.lpstrTitle = "Select a File";
//    ofn.Flags = OFN_PATHMUSTEXIST | OFN_FILEMUSTEXIST;
//
//    // 显示对话框
//    if (GetOpenFileName(&ofn) == TRUE) {
//        return file_name;
//    } else {
//        return NULL;
//    }
//}

#include <windows.h>
#include <stdio.h>

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
