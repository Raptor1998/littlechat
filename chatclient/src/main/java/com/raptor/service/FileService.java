package com.raptor.service;

import com.raptor.connect.ClientConnectServerThreadManager;
import com.raptor.entity.Message;
import com.raptor.entity.MessageType;

import java.io.*;
import java.util.Date;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/11  21:25
 */
public class FileService {

    public void sendFile(String src, String dest, String senderId, String getterId) {
        //读取文件
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_FILE_MESSAGE);
        message.setSendTime(new Date().toString());
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);

        System.out.println(senderId + " 给 " + getterId + "发送文件：" + src + " 到 " + dest);
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int) new File(src).length()];
        try {

            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ClientConnectServerThreadManager.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
