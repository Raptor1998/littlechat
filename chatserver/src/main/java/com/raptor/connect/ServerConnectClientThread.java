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

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("服务端线程，保持与客户端通信 " + userId);
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getMsgType().equals(MessageType.MESSAGE_GET_ONLINE)) {
                    System.out.println(message.getSender() + " 用户想要获取在线用户");
                    String onlineUsers = ServerConnectClientThreadManager.getOnlineUsers();
                    Message onlineUsersList = new Message();
                    onlineUsersList.setContent(onlineUsers);
                    onlineUsersList.setMsgType(MessageType.MESSAGE_GET_RETURN_ONLINE);
                    onlineUsersList.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    System.out.println("当前在线的用户：" + onlineUsers);
                    oos.writeObject(onlineUsersList);
                } else if (message.getMsgType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(userId + " 退出");
                    ServerConnectClientThreadManager.remove(message.getSender());
                    socket.close();
                    //当socket关闭之后   退出while循环
                    break;
                } else if (message.getMsgType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //消息转发
                    System.out.println(message.getSendTime() + " " + message.getSender() + " 对 " + message.getGetter()+ " 说：" + message.getContent());
                    ServerConnectClientThread serverConnectClientThread = ServerConnectClientThreadManager.getServerConnectClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    //如果用户不在线，则保存到数据库，可实现离线留言
                    oos.writeObject(message);
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
