#include <windows.h>
#include <stdbool.h>
#include "../main.h"

// 启动时指定窗口句柄
void flashWindow(HWND hwnd, int uCount, int dwTimeout) {
    FLASHWINFO fi;
    fi.cbSize = sizeof(FLASHWINFO);
    fi.hwnd = hwnd;
    fi.dwFlags = FLASHW_ALL;  // 任务栏和窗口标题都闪烁
    fi.uCount = uCount;  // 闪烁次数
    fi.dwTimeout = dwTimeout;  // 闪烁间隔（毫秒）

    FlashWindowEx(&fi);
}

// 根据窗口名进行闪烁
bool warn(const wchar_t *windowName) {
    // 查找窗口句柄
    HWND targetHwnd = FindWindowW(NULL, windowName);

    // 如果找到窗口，则进行闪烁
    if (targetHwnd != NULL) {
        flashWindow(targetHwnd, 1, 500);  // 对目标窗口闪烁
        return true;
    } else {
        // 如果没有找到窗口
        return false;
    }
}