package cn.com.Inet3;

import java.io.*;
import java.net.Socket;

public class Client2 {
    public static void main(String[] args) throws IOException {
        //使用多线程实现多个客户可以正常收发多条消息
        System.out.println("------client-------");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入用户名：");
        String name = br.readLine();
        Socket client = new Socket("localhost",6666);
        //客户端发送消息
        new Thread(new Send(client,name)).start();
        new Thread(new Receive(client)).start();
    }
}
