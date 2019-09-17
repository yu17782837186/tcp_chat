package cn.com.Inet3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Chat {
    //在线聊天室：服务器
    //目标：实现一个客户可以正常收发多条消息
    public static void main(String[] args) throws IOException {
        System.out.println("-----server-----");
        ServerSocket server = new ServerSocket(6666);
        Socket client = server.accept();
        System.out.println("一个客户端建立了连接");
        //接受消息
        DataInputStream dis = new DataInputStream(client.getInputStream());
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        boolean isRunning = true;
        while(isRunning) {
            String datas = dis.readUTF();
            //返回消息
            dos.writeUTF(datas);
            dos.flush();
        }
        //释放资源
        dos.close();
        dis.close();
        client.close();
    }
}
