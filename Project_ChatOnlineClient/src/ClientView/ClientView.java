package ClientView;

import ClientService.ClientService;
import ClientService.ClientChatService;
import ClientService.ClientFileService;
import java.util.Scanner;

/**
 * 客户端视图界面
 */
public class ClientView {

    public static void main(String[] args) {
        new ClientView().mainView();
    }
    private boolean loop = true; //控制是否显示界面
    private String key = ""; //接收用户的键盘输入
    private ClientService clientService = new ClientService(); //用于用户登录验证、用户注册等功能
    private ClientChatService clientChatService = new ClientChatService(); //提供聊天相关的方法
    private ClientFileService clientFileService = new ClientFileService(); //用于传输文件

    Scanner scanner = new Scanner(System.in);

    //显示主菜单
    public void mainView() {
        while (loop) {
            System.out.println("======欢迎使用网络通讯系统======");
            System.out.println("\t\t 1. 登录系统");
            System.out.println("\t\t 9. 退出系统");
            System.out.println("请输入1或9");

            key = scanner.next();
            switch (key) {
                case "1":
                    System.out.println("登录系统");
                    System.out.print("请输入用户名：");
                    String userID = scanner.next();
                    System.out.print("请输入密码：");
                    String passWord = scanner.next();
                    //传入服务器验证
                    if (clientService.checkLogin(userID, passWord)) {
                        //进入二级界面
                        System.out.println("======欢迎" + userID + "使用网络通讯系统======");
                        while (loop) {
                            System.out.println("======用户" + userID + "使用网络通讯系统======");
                            System.out.println("\t\t 1. 显示在线好友列表");
                            System.out.println("\t\t 2. 群发消息");
                            System.out.println("\t\t 3. 私聊消息");
                            System.out.println("\t\t 4. 发送文件");
                            System.out.println("\t\t 9. 退出系统");
                            System.out.println("请输入以上数字");

                            key = scanner.next();
                            switch (key) {
                                case "1":
                                    clientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.print("请输入想对所有人说的话：");
                                    String content = scanner.next();
                                    clientChatService.chatEveryone(userID, content);
                                    break;
                                case "3":
                                    System.out.print("请输入私聊目标ID：");
                                    String getterID = scanner.next();
                                    System.out.print("请输入私聊内容：");
                                    content = scanner.next();
                                    clientChatService.chatSomeone(userID, getterID, content);
                                    break;
                                case "4":
                                    System.out.print("请输入目标用户：");
                                    getterID = scanner.next();
                                    System.out.print("请输入源文件路径：");
                                    String src = scanner.next();
                                    System.out.print("请输入目标路径：");
                                    String dest = scanner.next();
                                    clientFileService.sendFileSomeone(userID, getterID, src, dest);
                                    break;
                                case "9":
                                    //给服务器发送退出退出系统的Message
                                    clientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("登录失败");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }
}
