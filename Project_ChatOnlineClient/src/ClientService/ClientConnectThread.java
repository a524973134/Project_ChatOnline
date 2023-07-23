package ClientService;

import Common.Message;
import Common.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 线程持有Socket，和服务器端保持通讯
 */
public class ClientConnectThread extends Thread {
    private Socket socket;

    public ClientConnectThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //线程需要保持和服务器通讯
        while (true) {
            //持续读取服务器消息
            try {
//                System.out.println("等待读取服务器端消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject(); //如果服务器没有发送Message，线程会阻塞

                //判断Message类型，做相应的业务处理
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    //取出在线列表信息并显示
                    String[] onlineFriend = message.getContent().split(" ");
                    System.out.println("======当前在线好友列表======");
                    for (int i = 0; i < onlineFriend.length; i++) {
                        System.out.println("用户： " + onlineFriend[i]);
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //普通私聊消息
                    System.out.println("(" + message.getSendTime() + ")" + message.getSender() + "对你说：" + message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_ALL_MES)) {
                    //群发
                    System.out.println("(" + message.getSendTime() + ")" + message.getSender() + "对所有人说：" + message.getContent());

                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    //文件
                    System.out.println(message.getSender() + "向你发送文件：" + message.getSrc());
                    //取出message中的字节数组，通过文件输出流写入磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("文件成功保存到：" + message.getDest());
                } else {
                    System.out.println("是其他类型信息");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
