cmake_minimum_required(VERSION 3.26)
project(bsdiff_win_master C)

set(CMAKE_C_STANDARD 11)

include_directories(bzip)

#add_executable
add_library(bsdiff_win_master SHARED
        bsdiff-win/bsdiff.c
        bspatch-win/bspatch.c
        bzip/blocksort.c
        bzip/bzlib.c
        bzip/bzlib.h
        bzip/bzlib_private.h
        bzip/compress.c
        bzip/crctable.c
        bzip/decompress.c
        bzip/huffman.c
        bzip/randtable.c
        main.c
        main.h
        jni/jni-bsdiff.c
        windows/pick_file.c
        windows/warn.c
)

# 设置库的输出文件名
set_target_properties(bsdiff_win_master PROPERTIES
    OUTPUT_NAME "bsdiff"  # 生成的 DLL 文件将名为 bsdiff-win.dll
    PREFIX ""             # 不使用前缀 "lib"（Windows 上不会用 "lib" 前缀）
    SUFFIX ".dll"         # 确保文件扩展名为 .dll
)