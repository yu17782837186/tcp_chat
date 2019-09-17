package cn.com.Inet3;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

//接收端  使用多线程封装了接收端
//接受消息 释放资源 重写run
public class Receive implements Runnable{
    private Socket client;
    private DataInputStream dis;
    private boolean isRunning;
    public Receive(Socket client) {
        this.client = client;
        try {
            dis = new DataInputStream(client.getInputStream());
            isRunning = true;
        } catch (IOException e) {
            System.out.println("*******1********");
            this.release();
        }
    }
    @Override
    public void run() {
        while(isRunning) {
            String msg = receive();
            if (!msg.equals("")) {
                System.out.println(msg);
            }
        }
    }
    private String receive() {
        try {
            String msg = dis.readUTF();
            return msg;
        } catch (IOException e) {
            System.out.println("*******3********");
            this.release();
        }
        return null;
    }
    private void release() {
        this.isRunning = false;
        Utiles.close(dis,client);
    }
}
