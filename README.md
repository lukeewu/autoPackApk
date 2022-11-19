# autoPackApk
全自动打包APK并签名
1：功能
    全自动打包APK并自动签名
2：要求：
    1：配置 apktool环境，将apktool.bat  apktool_2.6.1.jar 放在同一个文件夹，建在系统变量 path 添加
    1：需要手动反编译APK
         命令：apktool d E:\pack\yueyouhui\yueyouhui.apk -o E:\pack\yueyouhui\dcode
    2：反编译出来的文件删掉签名文件,路径-》E:\pack\yueyouhui\dcode\META-INF  除了MANIFEST.MF文件全部删除掉
	3：apk包不能加固
3：如何打包？
   执行 PackTest.buildApk方法，
4：思路
   1->反编译得到源文件
   2->修改AndroidManifest包名
   3->重新打包成APK
   4->对新的apk重新签名