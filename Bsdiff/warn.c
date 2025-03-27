#include <windows.h>
#include <stdbool.h>

void flashWindow(HWND hwnd,int uCount,int dwTimeout) {
    FLASHWINFO fi;
    fi.cbSize = sizeof(FLASHWINFO);
    fi.hwnd = hwnd;
    fi.dwFlags = FLASHW_ALL;  // 任务栏和窗口标题都闪烁
    fi.uCount = uCount;  // 闪烁次数
    fi.dwTimeout = dwTimeout;  // 闪烁间隔（毫秒）

    FlashWindowEx(&fi);
}

boolean warn() {
    HWND hwnd = GetForegroundWindow(); 
    if (hwnd) {
        flashWindow(hwnd,1,500);
        return true;
    } else {
        return false;
    }
}