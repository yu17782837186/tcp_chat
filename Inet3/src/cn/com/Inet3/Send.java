package cn.com.Inet3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


//发送端 使用了多线程封装了发送端
// 发送消息  从控制台获取消息 释放资源  重写run
public class Send implements Runnable{
    private Socket client;
    private BufferedReader console;
    private DataOutputStream dos;
    private boolean isRunning;
    private String name;
    public Send(Socket client,String name) {
        this.client = client;
        console = new BufferedReader(new InputStreamReader(System.in));
        try {
            dos = new DataOutputStream(client.getOutputStream());
            //管道建立好了 立即发送名称  在线程启动前将名称发过来
            send(name);
            this.isRunning = true;
            this.name = name;
        } catch (IOException e) {
            System.out.println("=====1====");
            this.release();
        }
    }
    @Override
    public void run() {
        while (isRunning) {
            String msg = getStrFromConsole();
            if (!msg.equals("")) {
                send(msg);
            }
        }
    }
    private void send(String msg) {
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            System.out.println("=====3====");
            this.release();
        }
    }
    //从控制台获取消息
    private String getStrFromConsole() {
        try {
            return console.readLine();
        } catch (IOException e) {
            System.out.println("=====2====");
            this.release();
        }
        return null;
    }

    private void release() {
        this.isRunning = false;
        Utiles.close(client,dos);
    }
}
