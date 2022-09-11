package com.raptor.service;

import com.raptor.connect.ClientConnectServerThread;
import com.raptor.connect.ClientConnectServerThreadManager;
import com.raptor.entity.Message;
import com.raptor.entity.MessageType;
import com.raptor.entity.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/11  12:55
 */
public class ChatService {
    public void sendMessage(String content, String senderId, String getterId) {
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_COMM_MES);
        message.setContent(content);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSendTime(new Date().toString());
        System.out.println(senderId + " 对 " + getterId + "说： " + content);
        try {
            ClientConnectServerThread clientConnectServerThread = ClientConnectServerThreadManager.getClientConnectServerThread(senderId);
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
