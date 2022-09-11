package com.raptor.connect;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/11  09:24
 */
public class ServerConnectClientThreadManager {
    private static HashMap<String, ServerConnectClientThread> map = new HashMap<>();

    public static void addServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        map.put(userId, serverConnectClientThread);
    }


    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return map.get(userId);
    }


    public static String getOnlineUsers() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            stringBuilder.append(iterator.next() + " " );
        }
        return stringBuilder.toString();
    }
}
