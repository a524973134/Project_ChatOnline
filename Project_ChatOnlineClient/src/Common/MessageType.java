package Common;

/**
 * 消息类型
 * 不同常量表示不同消息类型
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1"; //表示登入成功
    String MESSAGE_LOGIN_FAIL = "2"; //表示登入失败
    String MESSAGE_COMM_MES = "3"; //普通信息包
    String MESSAGE_GET_ONLINE_FRIEND = "4"; //要求返回在线好友列表
    String MESSAGE_RET_ONLINE_FRIEND = "5"; //返回的在线好友列表
    String MESSAGE_CLIENT_EXIT = "6"; //客户端请求退出
    String MESSAGE_ALL_MES = "7"; //群发信息包
    String MESSAGE_FILE_MES = "8"; //文件消息
}
