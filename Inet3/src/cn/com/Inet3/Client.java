package cn.com.Inet3;

import java.io.*;
import java.net.Socket;

public class Client {
    //在线聊天室：客户端
    //目标：实现一个客户可以正常收发多条消息
    public static void main(String[] args) throws IOException {
        System.out.println("------client-------");
        Socket client = new Socket("localhost",6666);
        //客户端发送消息
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        DataInputStream dis = new DataInputStream(client.getInputStream());
        boolean isRunning = true;
        while(isRunning) {
            String msg = console.readLine();
            dos.writeUTF(msg);
            dos.flush();
            //获取消息
            msg = dis.readUTF();
            System.out.println(msg);
        }
        //释放资源
        dos.close();
        dis.close();
        client.close();
    }
}
