package com.code.packapk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Command {
    private Process process;
    private InputStream is;
    private OutputStream os;
    private BufferedWriter bw;
    private BufferedReader br;
    public Command() {}

    /**
     * 启动cmd控制台
     * @param cmd
     */
    public void setCmd(String cmd) throws IOException{
        process = Runtime.getRuntime().exec(cmd);
        os = process.getOutputStream();
        is = process.getInputStream();
    }
    /**
     * 往cmd控制台输入信息
     * @return
     */
    public void writeCmd(String cmd) throws IOException{
        bw = new BufferedWriter(new OutputStreamWriter(os));
        bw.write(cmd);
        bw.newLine();
        bw.flush();
        bw.close();
    }
    /**
     * 打印cmd控制台信息
     * @return
     */
    public String readCmd() throws IOException,InterruptedException{
        StringBuffer sb = new StringBuffer();
        br = new BufferedReader(new InputStreamReader(is));
        String buffer ;
        while ((buffer = br.readLine()) != null) {
            sb.append(buffer + "\n");
        }
        System.out.println(process.waitFor());
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            Command cmd = new Command();
            cmd.setCmd("cmd");
            cmd.writeCmd("d:\\apktool\\apktool.bat b F:\\apk\\apks -o F:\\apk\\test.apk");
            System.out.println(cmd.readCmd());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
