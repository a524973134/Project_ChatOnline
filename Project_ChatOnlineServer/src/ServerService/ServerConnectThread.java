package ServerService;

import Common.MessageType;
import Common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类对象用于保持服务端和客户端通讯
 */
public class ServerConnectThread extends Thread {
    private Socket socket;
    private String userID; //连接到服务端的客户ID

    public ServerConnectThread(Socket socket, String userID) {
        this.socket = socket;
        this.userID = userID;
    }

    @Override
    public void run() {
        //服务端发送、接收消息
        while (true) {
            try {
                System.out.println("服务器与用户" + userID + "保持通讯");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                //根据Message类型，做出相应的业务处理
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    //客户端请求返回在线好友列表
                    System.out.println(message.getSender() + "请求在线好友列表");
                    String onlineFriend = ServerConnectThreadManage.getOnlineFriend();

                    //构建Message对象返回onlineFriend给客户端
                    Message message1 = new Message();
                    message1.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message1.setContent(onlineFriend);
                    message1.setGetter(message.getSender());

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message1);
                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    //客户端请求退出
                    System.out.println(message.getSender() + "退出系统");
                    //将该客户端对应的线程从集合中移除
                    ServerConnectThreadManage.removeServerConnectThread(message.getSender());
                    //关闭连接
                    socket.close();
                    //退出while循环
                    break;
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //私聊
                    //将一个线程收到的Message对象转发给另一个线程所连接的客户端
                    ServerConnectThread serverConnectThread = ServerConnectThreadManage.getServerConnectThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectThread.socket.getOutputStream());
                    oos.writeObject(message);
                } else if (message.getMesType().equals(MessageType.MESSAGE_ALL_MES)) {
                    //群发
                    //遍历所有线程，排除sender
                    HashMap<String, ServerConnectThread> hm = ServerConnectThreadManage.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        //取出除了群发者的在线好友ID
                        String onlineFriend = iterator.next().toString();
                        if (!onlineFriend.equals(message.getSender())) {
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onlineFriend).socket.getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    //根据getter的ID获取对应线程
                    ObjectOutputStream oos = new ObjectOutputStream(ServerConnectThreadManage.getServerConnectThread(message.getGetter()).socket.getOutputStream());
                    oos.writeObject(message);
                } else {
                    System.out.println("其他类型信息");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
