package com.raptor.service;

import com.raptor.connect.ClientConnectServerThread;
import com.raptor.connect.ClientConnectServerThreadManager;
import com.raptor.entity.Message;
import com.raptor.entity.MessageType;
import com.raptor.entity.User;
import sun.java2d.OSXSurfaceData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/10  23:38
 */
public class ClientService {
    private User user = new User();

    private Socket socket;

    public boolean checkUser(String userId, String password) {
        user.setUserId(userId);
        user.setPassword(password);
        boolean flag = false;
        //连接服务器
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            if (message.getMsgType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)) {
                //创建一个和服务器  保持通信的线程
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                flag = true;
                ClientConnectServerThreadManager.addClientConnectServerThread(userId, clientConnectServerThread);
            } else {
                //登录失败
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    public void onlineList() {
        //发送message  获取在线用户

        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_GET_ONLINE);
        message.setSender(user.getUserId());
        try {
            //获取当前用户对应的线程对象
            ObjectOutputStream oos = new ObjectOutputStream(ClientConnectServerThreadManager.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void logout() {
        try {
            Message message = new Message();
            message.setMsgType(MessageType.MESSAGE_CLIENT_EXIT);
            message.setSender(user.getUserId());
            ObjectOutputStream oos = new ObjectOutputStream(ClientConnectServerThreadManager.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());

            oos.writeObject(message);

            System.out.println(user.getUserId() + " 退出系统");
            //结束当前线程
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
