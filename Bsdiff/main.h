//
// Created by Chiu-xaH on 2025/3/21.
//

#ifndef BSDIFF_WIN_MASTER_MAIN_H
#define BSDIFF_WIN_MASTER_MAIN_H

int patch(const char *oldFile, const char *newFile, const char *patchFile);
int merge(const char *oldFile, const char *patchFile, const char *newFile);
__declspec(dllexport) const char* pick_file();

#endif //BSDIFF_WIN_MASTER_MAIN_H
