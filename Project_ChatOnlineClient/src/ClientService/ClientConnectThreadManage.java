package ClientService;

import java.util.HashMap;

/**
 * 管理保持客户端与服务器端通讯的所有线程
 */
public class ClientConnectThreadManage {
    //用集合管理多个线程
    private static HashMap<String, ClientConnectThread> hm = new HashMap<>();

    //将线程加入集合
    public static void addclientConnectThread(String userID, ClientConnectThread clientConnectThread) {
        hm.put(userID, clientConnectThread);
    }

    //通过userID将线程取出
    public static ClientConnectThread getClientConnectThread(String userID) {
        return hm.get(userID);
    }
}
