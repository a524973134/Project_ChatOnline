package ClientService;

import Common.Message;
import Common.MessageType;

import java.io.*;

/**
 * 该类完成文件传输功能
 */
public class ClientFileService {
    /**
     * 发送文件
     * @param sender 发送者
     * @param getter 接收者
     * @param src 源文件
     * @param dest 目标路径
     */
    public void sendFileSomeone(String sender, String getter, String src, String dest) {
        //设置文件message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(sender);
        message.setGetter(getter);
        message.setSrc(src);
        message.setDest(dest);

        //文件读取
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            //将src读入字节数组
            fileInputStream.read(fileBytes);
            //将字节数组放入message对象
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ClientConnectThreadManage.getClientConnectThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(sender + "向" + getter + "发送文件：" + src);
    }
}
