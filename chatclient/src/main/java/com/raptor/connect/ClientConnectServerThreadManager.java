package com.raptor.connect;

import javax.xml.ws.soap.Addressing;
import java.util.HashMap;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/10  23:53
 */
public class ClientConnectServerThreadManager {

    private static HashMap<String,ClientConnectServerThread> map = new HashMap<>();


    public static void addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread){
        map.put(userId,clientConnectServerThread);
    }

    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return map.get(userId);
    }
}
