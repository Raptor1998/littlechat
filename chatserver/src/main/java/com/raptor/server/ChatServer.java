package com.raptor.server;

import com.raptor.connect.ServerConnectClientThread;
import com.raptor.connect.ServerConnectClientThreadManager;
import com.raptor.entity.Message;
import com.raptor.entity.MessageType;
import com.raptor.entity.User;
import com.raptor.service.SendToAllService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/11  08:59
 */
public class ChatServer {

    private ServerSocket serverSocket = null;
    private static ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();

    static {
        userMap.put("raptor", new User("raptor", "123"));
        userMap.put("bytedance", new User("bytedance", "123"));
        userMap.put("npc3", new User("npc3", "123"));
        userMap.put("npc1", new User("npc1", "123"));
        userMap.put("npc2", new User("npc2", "123"));
    }

    public ChatServer() {
        System.out.println("server is ready");
        try {
            serverSocket = new ServerSocket(9999);
            //启动推送线程
            new Thread(new SendToAllService()).start();
            while (true) {
                //持续监听
                Socket socket = serverSocket.accept();
                //得到输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message message = new Message();
                //验证用户名密码
                if (checkUser(user.getUserId(), user.getPassword())) {
                    message.setMsgType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    //返回客户端信息
                    oos.writeObject(message);
                    //创建一个线程和客户端保持通信
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserId());
                    serverConnectClientThread.start();
                    ServerConnectClientThreadManager.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);
                } else {
                    //登录失败
                    System.out.println(user.getUserId() + "用户名或密码错误");
                    message.setMsgType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //server退出while循环
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean checkUser(String userId, String password) {
        User user = userMap.get(userId);
        if (user == null) {
            return false;
        }
        if (user.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}
