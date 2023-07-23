package ClientService;

import Common.Message;
import Common.MessageType;
import Common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 完成用户登录验证、用户注册等功能
 */
public class ClientService {
    //将User做成属性，因为可能在其他地方用到User信息
    private User user = new User();
    //将Socket做成属性，因为可能在其他地方用到Socket信息，例如放入集合中统一管理
    private Socket socket;

    //根据ID和密码创建User对象传入服务器验证
    public boolean checkLogin(String userID, String passWord) {
        boolean feedback = false;
        //根据ID和密码创建User对象
        user.setUserID(userID);
        user.setPassWord(passWord);

        //传入服务器验证
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            //得到ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //将User对象传入服务器
            oos.writeObject(user);

            //读取从服务端反馈的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();

            if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
                //启动一个线程，线程持有Socket，和服务器端保持通讯
                ClientConnectThread clientConnectThread = new ClientConnectThread(socket);
                //启动与服务器保持通讯的线程
                clientConnectThread.start();
                //将该线程放入集合统一管理
                ClientConnectThreadManage.addclientConnectThread(userID, clientConnectThread);
                feedback = true;
            } else {
                //登录失败，关闭Socket
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return feedback;
    }

    //向服务器端请求返回在线列表
    public void onlineFriendList() {
        //发送给服务器一个Message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserID());

        //得到当前线程的Socket获取的oos
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ClientConnectThreadManage.getClientConnectThread(user.getUserID()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //退出客户端，并给服务端发送退出系统的Message
    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserID()); //指定要退出的客户端

        try {
            //通过userID获取对应线程的Socket
            ObjectOutputStream oos = new ObjectOutputStream(ClientConnectThreadManage.getClientConnectThread(user.getUserID()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserID() + "退出系统");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
