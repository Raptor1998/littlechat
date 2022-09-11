package com.raptor.connect;

import com.raptor.entity.Message;
import com.raptor.entity.MessageType;
import com.raptor.server.ChatServer;
import com.sun.security.ntlm.Server;
import sun.java2d.OSXSurfaceData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
                    ServerConnectClientThread serverConnectClientThread = ServerConnectClientThreadManager.getServerConnectClientThread(message.getGetter());
                    if (serverConnectClientThread == null){
                        System.out.println(message.getGetter() + "不在线");
                        List<Message> messages = ChatServer.getOfflineMessage().get(message.getGetter());
                        if (messages == null){
                            messages = new ArrayList<>();
                        }
                        messages.add(message);
                        ChatServer.getOfflineMessage().put(message.getGetter(),messages);
                    }else {
                        System.out.println(message.getSendTime() + " " + message.getSender() + " 对 " + message.getGetter() + " 说：" + message.getContent());
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);
                    }
                } else if (message.getMsgType().equals(MessageType.MESSAGE_GROUP_MESSAGE)) {
                    //群发消息
                    System.out.println(message.getSender() + "对所有人说：" + message.getContent());
                    HashMap<String, ServerConnectClientThread> map = ServerConnectClientThreadManager.getMap();
                    Iterator<String> iterator = map.keySet().iterator();
                    while (iterator.hasNext()) {
                        String onlineId = iterator.next().toString();
                        if (!onlineId.equals(userId)) {
                            ObjectOutputStream oos = new ObjectOutputStream(map.get(onlineId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                } else if (message.getMsgType().equals(MessageType.MESSAGE_FILE_MESSAGE)) {
                    System.out.println(message.getSender() + " 给 " + message.getGetter() + "发送文件：" + message.getSrc() + " 到 " + message.getDest());
                    ObjectOutputStream oos = new ObjectOutputStream(ServerConnectClientThreadManager.getServerConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
