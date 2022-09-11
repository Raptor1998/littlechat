package com.raptor.connect;

import com.raptor.entity.Message;
import com.raptor.entity.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/10  23:46
 */
public class ClientConnectServerThread extends Thread {
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //需要再后台和服务器通信
        while (true) {
            try {
                System.out.println("客户端线程，等待从服务器接受消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                if (message.getMsgType().equals(MessageType.MESSAGE_GET_RETURN_ONLINE)) {
                    System.out.println("正在请求所有在线用户列表");
                    String[] onlineUsers = message.getContent().split(" ");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户：" + onlineUsers[i]);
                    }
                } else if (message.getMsgType().equals(MessageType.MESSAGE_COMM_MES)) {
                    System.out.println(message.getSendTime() + " 收到来自 " + message.getSender() + " 的消息：" + message.getContent());
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
