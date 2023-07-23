package ServerService;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类用于管理用于服务端和客户端通讯的所有线程
 */
public class ServerConnectThreadManage {
    private static HashMap<String, ServerConnectThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectThread> getHm() {
        return hm;
    }

    //添加线程到集合
    public static void addServerConnectThread(String userID, ServerConnectThread serverConnectThread) {
        hm.put(userID, serverConnectThread);
    }

    //移除userID对应的线程
    public static void removeServerConnectThread(String userID) {
        hm.remove(userID);
    }

    //根据userID返回ServerConnectThread
    public static ServerConnectThread getServerConnectThread(String userID) {
        return hm.get(userID);
    }

    //返回在线好友列表
    public static String getOnlineFriend() {
        //遍历集合的key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineFriendList = "";
        while (iterator.hasNext()) {
            onlineFriendList += iterator.next().toString() + " ";
        }
        return onlineFriendList;
    }
}
