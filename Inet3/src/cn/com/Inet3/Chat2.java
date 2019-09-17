package cn.com.Inet3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.CopyOnWriteArrayList;

public class Chat2 {
    private static CopyOnWriteArrayList<Channel> all = new CopyOnWriteArrayList<>();
    //加入容器实现群聊
    public static void main(String[] args) throws IOException {
        //使用多线程实现多个客户可以正常收发多条消息
        //问题 其它客户必须等待之前的客户退出，才能继续 排队 加入多线程解决问题
        //问题 代码不好维护  客户端读写没有分开 必须先写后读
        System.out.println("-----server-----");
        ServerSocket server = new ServerSocket(6666);
        while(true) {
            Socket client = server.accept();
            System.out.println("一个客户端建立了连接");
            Channel c = new Channel(client);
            all.add(c);//管理所有的成员
            new Thread(c).start();
        }
    }
    //一个客户代表一个Channel
    static class Channel implements Runnable{

        private Socket client;
        private DataInputStream dis;
        private DataOutputStream dos;
        private boolean isRunning;
        private String name;
        public Channel(Socket client) {
            this.client = client;
            try {
                dis = new DataInputStream(client.getInputStream());
                dos = new DataOutputStream(client.getOutputStream());
                isRunning = true;
                //获取名称
                this.name = receive();
                this.send("欢迎你的到来");
                sendOthers(this.name+"来到了余小浩的聊天室",true);
            } catch (IOException e) {
                System.out.println("-----1-----");
                release();
            }
        }
        //接收消息
        private String receive() {
            try {
                String msg = dis.readUTF();
                return msg;
            } catch (IOException e) {
                System.out.println("-----2-----");
                release();
            }
            return null;
        }
        //发送消息
        private void send(String msg) {
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                System.out.println("-----3-----");
                release();
            }
        }
        //群聊
        //私聊 约定数据格式@xxx:msg
        private void sendOthers(String msg,boolean isSys) {
            boolean isPrivate = msg.startsWith("@");
            if (isPrivate) {//私聊
                int index = msg.indexOf(":");
                //获取目标和数据
                String targetName = msg.substring(1,index);
                msg = msg.substring(index+1);
                for (Channel other:all) {
                    if (other.name.equals(targetName)) {
                        other.send(this.name+"悄悄地对你说："+msg);
                        break;
                    }
                }
            }else {//群聊
                for (Channel other : all) {
                    if (other == this) {//自己
                        continue;
                    }
                    if (!isSys) {
                        other.send(this.name + "对所有人说：" + msg); //群聊消息
                    } else {
                        other.send(msg); //系统消息
                    }
                }
            }
        }
        //释放资源
        private void release() {
            this.isRunning = false;
            Utiles.close(dis,dos,client);
            //退出
            all.remove(this);
            sendOthers(this.name+"离开了余小浩的聊天室",true);
        }

        @Override
        public void run() {
            while(isRunning) {
                String msg = receive();
                if (!msg.equals("")) {
                    //send(msg);
                    sendOthers(msg,false);//发给其他人
                }
            }
        }
    }
}
