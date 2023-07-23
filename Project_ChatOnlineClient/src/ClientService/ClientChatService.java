package ClientService;

import Common.Message;
import Common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 该类提供聊天相关的方法
 */
public class ClientChatService {
    //私聊方法
    public void chatSomeone(String sender, String getter, String content) {
        //构建Message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(sender);
        message.setGetter(getter);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());
        System.out.println("(" + message.getSendTime() + ")" + sender + "对" + getter + "说：" + content);

        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ClientConnectThreadManage.getClientConnectThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //群发方法
    public void chatEveryone(String sender, String content) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_ALL_MES);
        message.setSender(sender);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());
        System.out.println("(" + message.getSendTime() + ")" + sender + "对所有人说：" + content);

        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ClientConnectThreadManage.getClientConnectThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

