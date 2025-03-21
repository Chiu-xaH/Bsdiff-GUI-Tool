# Bsdiff算法 用于增量更新二进制文件

从网上摘抄的，经过简单封装，方便开箱即用，可运行merge与patch函数进行合并补丁包和生成补丁包功能

## 食用方法

main.c中可以调用main.h定义的两个函数，也包含了示例调用函数

实测生成补丁包时文件较大时耗时较长，合并补丁包时相对速度比较快
```C
// 生成补丁包,返回值为0则成功
int patch(const char *oldFile, const char *newFile, const char *patchFile);
// 合并补丁包，返回值为1则成功
int merge(const char *oldFile, const char *patchFile, const char *newFile);
```

用于Android开发中的增量更新，可以编译成so库，以JNI调用，预设的JNI代码在./jni/jni-bsdiff.c中已有，两个方法需根据自己项目类的路径修改