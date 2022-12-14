package com.raptor.view;

import com.raptor.service.ChatService;
import com.raptor.service.ClientService;
import com.raptor.service.FileService;
import com.raptor.util.Utility;

import java.io.File;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/10  23:24
 */
public class ClientView {

    private boolean loop = true;
    private String key = "";

    private ClientService clientService = new ClientService();

    private ChatService chatService = new ChatService();

    private FileService fileService = new FileService();

    public void mainView() {
        while (loop) {
            System.out.println("=========little chat=======");
            System.out.println("\t\t 1 登录");
            System.out.println("\t\t 9 退出");

            key = Utility.readString(1);
            switch (key) {
                case "1":
                    System.out.println("please input username: ");
                    String userId = Utility.readString(50);
                    System.out.println("please input password: ");
                    String password = Utility.readString(50);
                    //去服务器验证结果

                    if (clientService.checkUser(userId, password)) {
                        System.out.println("welcome user:" + userId);
                        while (loop) {
                            System.out.println("=========little chat for user:" + userId + "=======");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出");
                            System.out.println("请输入：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    clientService.onlineList();
                                    break;
                                case "2":
                                    System.out.println("请输入群发消息：");
                                    String msg = Utility.readString(100);
                                    chatService.sendGroupMessage(msg, userId);
                                    break;
                                case "3":
                                    System.out.println("输入聊天对象：");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话：");
                                    String content = Utility.readString(100);
                                    //发送消息
                                    chatService.sendMessage(content, userId, getterId);
                                    System.out.println("私聊消息：");
                                    break;
                                case "4":
                                    System.out.println("请输入接收文件的对象");
                                    String fileGetterId = Utility.readString(50);
                                    System.out.println("请输入发送文件路径：");
                                    String sendPath = Utility.readString(100);

                                    System.out.println("请输入发送文件存储到的路径：");
                                    String savePath = Utility.readString(100);
                                    fileService.sendFile(sendPath, savePath, userId, fileGetterId);
                                    break;
                                case "9":
                                    loop = false;
                                    clientService.logout();
                                    break;
                                default:
                                    System.out.println("error key");
                                    break;
                            }
                        }
                    } else {
                        System.out.println(userId + "账号或密码错误");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
                default:
                    System.out.println("error key");
                    break;
            }

        }
    }

    public static void main(String[] args) {
        new ClientView().mainView();
    }
}
