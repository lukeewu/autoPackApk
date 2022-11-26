package com.code.packapk;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.UUID;
import java.io.File;
import java.io.FileWriter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class PackTest {

    /**
     * apk自动打包总入口
     */
    @Test
    public void buildApk(){
        //打包约友会
        packYueyouhui(2);
        //打包约视频
        //packYueshipin(15);
        //打包看视频
        //packKanshipin(10);
        System.out.println("打包完成！！！！！！！！");
    }

    /**
     * 打包约友会APK
     * @param packNum  每次需要打包的数量
     */
    public void packYueyouhui(int packNum){
        String folderPath = "E:\\pack\\yueyouhui\\dcode";
        String srcApkFolder = "E:\\pack\\yueyouhui\\";
        String descApkFolder = "E:\\pack\\yueyouhui\\apk\\";
        for(int i= 1;i<=packNum;i++){
            String destApkName ="yueyouhui"+System.currentTimeMillis()+".apk";
            String packageName = "com.mitao.yueyouhui"+UUID.randomUUID().toString().replaceAll("-","");
            //开始打包
            System.out.println("-----------------yueyouhui"+"第"+i+"个APK打包开始-----------------");
            pack(folderPath,srcApkFolder,descApkFolder,destApkName,packageName);
        }
    }
    /**
     * 打包约视频 APK
     * @param packNum  每次需要打包的数量
     */
    public void packYueshipin(int packNum){
        String folderPath = "E:\\pack\\yueshipin\\dcode";
        String srcApkFolder = "E:\\pack\\yueshipin\\";
        String descApkFolder = "E:\\pack\\yueshipin\\apk\\";
        for(int i= 1;i<=packNum;i++){
            String destApkName ="yueshipin"+System.currentTimeMillis()+".apk";
            String packageName = "com.mitao.yueshipin"+UUID.randomUUID().toString().replaceAll("-","");
            //开始打包
            System.out.println("-----------------yueshipin"+"第"+i+"个APK打包开始-----------------");
            pack(folderPath,srcApkFolder,descApkFolder,destApkName,packageName);
        }
    }
    /**
     * 打包看视频 APK
     * @param packNum  每次需要打包的数量
     */
    public void packKanshipin(int packNum){
        String folderPath = "E:\\pack\\kanshipin\\dcode";
        String srcApkFolder = "E:\\pack\\kanshipin\\";
        String descApkFolder = "E:\\pack\\kanshipin\\apk\\";
        for(int i= 1;i<=packNum;i++){
            String destApkName ="kanshipin"+System.currentTimeMillis()+".apk";
            String packageName = "com.mitao.kanshipin"+UUID.randomUUID().toString().replaceAll("-","");
            //开始打包
            System.out.println("-----------------kanshipin"+"第"+i+"个APK打包开始-----------------");
            pack(folderPath,srcApkFolder,descApkFolder,destApkName,packageName);
        }
    }

    /**
     * 执行打包流程
     * @param folderPath 需要打包的文件夹（该文件夹为APK反编译得到的文件夹，并且需要删除签名文件 即original\META-INF下除了MANIFEST.MF 需要全部删除）
     * @param srcApkFolder 打包完成（未签名）之后存放的文件夹
     * @param descApkFolder 打包完成并且签名之后存放的文件夹
     * @param destApkName   签名后的apk名称
     * @param packageName   apk里面的包名
     */
    public void pack(String folderPath,String srcApkFolder,String descApkFolder,String destApkName, String packageName){
        try {
            //1: 修改包名
            changePackageName(packageName,folderPath);
            //2：打包
            String srcApkName = destApkName;
            String packCommand = "d:\\apktool\\apktool.bat b "+folderPath+" -o "+srcApkFolder+srcApkName;
            packApk(packCommand);
            //3：生成签名
            String keystoreName = UUID.randomUUID().toString().replaceAll("-","");
            creatKeyStore(srcApkFolder,keystoreName);
            //4：给APK签名
            String signCommand = "jarsigner -verbose -keystore "+srcApkFolder+keystoreName+".keystore -signedjar "+descApkFolder+destApkName+" "+srcApkFolder+srcApkName+" key0 -storepass "+keystoreName;
            signApk(signCommand);
            //签名完成删掉源文件
            FileUtil.del(srcApkFolder+keystoreName);
            FileUtil.del(srcApkFolder+srcApkName);
        }catch (Exception e){
            //只要出现异常就中断
            e.printStackTrace();
        }

    }

    /**
     * 修改包名
     * @param packageName
     * @param folderPath
     * @throws Exception
     */
    public void changePackageName(String packageName,String folderPath) throws Exception{
        String filePath = folderPath + "\\AndroidManifest.xml";
        //"F:\\apk\\apks\\AndroidManifest.xml";
        //读取XML文件，获得document对象
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(filePath));
        //获得某个节点的属性对象
        Element rootElem = document.getRootElement();
        //修改根目录包名
        rootElem.attribute("package").setValue(packageName);
        //修改子节点包名
        Element oneChildElem = rootElem.element("application");
        oneChildElem.element("provider").attribute(0).setValue(packageName+".lifecycle-process");
        //将某节点的属性和值写入xml文档中
        XMLWriter writer = new XMLWriter(new FileWriter(filePath));
        writer.write(document);
        writer.close();
        System.out.println("-----------------修改包名成功-----------------");
    }
    /**
     * 打包APK
     * @param packCommand
     * @throws Exception
     */
    public void packApk(String packCommand) throws Exception{
        Command packCmd = new Command();
        packCmd.setCmd("cmd");
        packCmd.writeCmd(packCommand);
        System.out.println(packCmd.readCmd());
        System.out.println("-----------------打包完成-----------------");
    }
    /**
     * 签名APK
     * @param signCommand 签名命令
     * @throws InterruptedException
     * @throws IOException
     */
    public void signApk(String signCommand) throws InterruptedException,IOException{
        Command signCmd = new Command();
        signCmd.setCmd("cmd");
        signCmd.writeCmd(signCommand);
        System.out.println(signCmd.readCmd());
        System.out.println("-----------------签名完成-----------------");
    }
    /**
     * 生成签名文件
     * @param keystoreName 作为签名文件名称，也作为密码
     * @throws Exception
     */
    public static void creatKeyStore(String srcApkFolder,String keystoreName) throws Exception{
        //签名文件和打包出来的apk放一个目录，方便签名
        String filePath = srcApkFolder+keystoreName+".keystore";
        int keysize = 1024;   // 大小
        String commonName = "ttll";
        final String organizationalUnit = "tl"+System.currentTimeMillis();
        final String organization = "tl"+System.currentTimeMillis();
        final String city = "California";
        final String state = "California";
        final String country = "US";
        final long validity = 365 * 50; // 证书的有效期
        String alias = "key0";    // 别名
        char[] storePass = keystoreName.toCharArray(); // 秘钥库口令   签名文件密码
        char[] keyPass = keystoreName.toCharArray(); // 别名密码   私钥
        // 初始化一个keystore对象   keystore文件对应JKS实例  ios的p12文件对应PKCS12实例
        KeyStore ks = KeyStore.getInstance("JKS");
        // 由于是新创建keystore文件参数传null
        ks.load(null, null);
        // 签名文件加密方式和签名文件常规信息
        CertAndKeyGen keypair = new CertAndKeyGen("RSA", "SHA1WithRSA", null);
        X500Name x500Name = new X500Name(commonName, organizationalUnit, organization, city, state, country);
        keypair.generate(keysize);
        // 创建私钥对象及设置证书的有效期
        PrivateKey privateKey = keypair.getPrivateKey();
        X509Certificate[] chain = new X509Certificate[1];
        chain[0] = keypair.getSelfCertificate(x500Name, new Date(), (long)validity*24*60*60);
        // store away the key store
        FileOutputStream fos = new FileOutputStream(filePath);
        // 设置别名私钥密码
        ks.setKeyEntry(alias, privateKey, keyPass, chain);
        // 设置签名文件密码   秘钥库口令
        ks.store(fos, storePass);
        fos.close();
        System.out.println("-----------------生成签名文件成功！-----------------");
    }
}
