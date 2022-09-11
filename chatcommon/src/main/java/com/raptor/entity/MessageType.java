package com.raptor.entity;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/10  22:58
 */
public interface MessageType {

    String MESSAGE_LOGIN_SUCCESS = "1";
    String MESSAGE_LOGIN_FAIL = "2";

    String MESSAGE_COMM_MES = "3";
    String MESSAGE_GET_ONLINE = "4";
    String MESSAGE_GET_RETURN_ONLINE = "5";

    String MESSAGE_GROUP_MESSAGE  = "7";
    String MESSAGE_FILE_MESSAGE  = "8";
    //客户端请求退出
    String MESSAGE_CLIENT_EXIT = "6";
}
