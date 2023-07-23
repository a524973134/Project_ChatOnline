package Common;

import java.io.Serializable;

/**
 * 客户端和服务端通讯时的消息对象
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L; //增加兼容性
    private String sender; //发送者
    private String getter; //接收者
    private String content; //内容
    private String sendTime; //发送时间
    private String mesType; //消息类型，在接口MessageType中定义

    //和文件相关的属性
    private byte[] fileBytes;
    private int fileLen = 0; //文件长度
    private String dest; //传输目标地址
    private String src; //源文件地址


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
