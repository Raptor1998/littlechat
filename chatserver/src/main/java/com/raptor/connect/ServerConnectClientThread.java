package com.raptor.connect;

import com.raptor.entity.Message;
import com.raptor.entity.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/11  09:19
 */
public class ServerConnectClientThread extends Thread {
    private Socket socket;

    private String userId;

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("服务端线程，保持与客户端通信 " + userId);
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if(message.getMsgType().equals(MessageType.MESSAGE_GET_ONLINE)){
                    System.out.println(message.getSender() + " 用户想要获取在线用户");
                    String onlineUsers = ServerConnectClientThreadManager.getOnlineUsers();
                    Message onlineUsersList = new Message();
                    onlineUsersList.setContent(onlineUsers);
                    onlineUsersList.setMsgType(MessageType.MESSAGE_GET_RETURN_ONLINE);
                    onlineUsersList.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    System.out.println("当前在线的用户：" + onlineUsers);
                    oos.writeObject(onlineUsersList);
                }else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
