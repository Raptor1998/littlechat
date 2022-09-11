package com.raptor.service;

import com.raptor.connect.ServerConnectClientThread;
import com.raptor.connect.ServerConnectClientThreadManager;
import com.raptor.entity.Message;
import com.raptor.entity.MessageType;
import com.raptor.util.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/11  23:05
 */
public class SendToAllService implements Runnable {

    @Override
    public void run() {
        while (true) {
            System.out.println("请输入已推送的消息：");

            String msg = Utility.readString(100);
            if ("exit".equals(msg)){
                break;
            }
            Message message = new Message();
            message.setSendTime(new Date().toString());
            message.setSender("服务器");
            message.setContent(msg);
            message.setMsgType(MessageType.MESSAGE_COMM_MES);
            System.out.println("服务器对所有人说：" + msg);
            HashMap<String, ServerConnectClientThread> map = ServerConnectClientThreadManager.getMap();
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String onlineId = iterator.next().toString();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(map.get(onlineId).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
