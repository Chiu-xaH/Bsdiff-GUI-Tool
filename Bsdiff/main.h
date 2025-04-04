//
// Created by Chiu-xaH on 2025/3/21.
//

#ifndef BSDIFF_WIN_MASTER_MAIN_H
#define BSDIFF_WIN_MASTER_MAIN_H

#include <stdbool.h>

bool patch(const char *oldFile, const char *newFile, const char *patchFile);
bool merge(const char *oldFile, const char *patchFile, const char *newFile);
__declspec(dllexport) const char* pick_file();
bool warn(const wchar_t *windowName);

#endif //BSDIFF_WIN_MASTER_MAIN_H
