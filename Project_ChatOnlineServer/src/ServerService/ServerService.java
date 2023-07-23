package ServerService;

import Common.Message;
import Common.MessageType;
import Common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * 服务器监听9999端口，等待客户端连接
 */
public class ServerService {
    private ServerSocket serverSocket = null;
    //创建集合模拟数据库存放用户信息
    private static HashMap<String, User> userInfo = new HashMap<>();
    //静态代码块在类加载时初始化用户信息
    static {
        userInfo.put("100", new User("100", "123456"));
        userInfo.put("200", new User("200", "123456"));
        userInfo.put("300", new User("300", "123456"));
    }

    //验证用户信息
    private boolean checkUserInfo(String userID, String passWord) {
        User user = userInfo.get(userID);
        if (user == null) { //说明该ID不存在
            return false;
        }
        if (!user.getPassWord().equals(passWord)) { //ID存在，但密码错误
            return false;
        }
        return true;
    }

    public ServerService() {
        try {
            //端口写在配置文件中
            System.out.println("服务端在9999端口监听");
            serverSocket = new ServerSocket(9999);

            //持续监听，所以放入循环中
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) ois.readObject();
                Message message = new Message();

                //验证用户
                if (checkUserInfo(user.getUserID(), user.getPassWord())) {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将message对象回复给客户端
                    oos.writeObject(message);

                    //创建线程，持有socket对象，和客户端保持通讯
                    ServerConnectThread serverConnectThread = new ServerConnectThread(socket, user.getUserID());
                    serverConnectThread.start();

                    //将线程对象放入集合中统一管理
                    ServerConnectThreadManage.addServerConnectThread(user.getUserID(), serverConnectThread);
                } else { //登录失败
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //如果服务端退出while循环，即服务端不再监听，所以要关闭所有资源
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
