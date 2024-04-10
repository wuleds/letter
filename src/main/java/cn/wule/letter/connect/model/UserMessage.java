package cn.wule.letter.connect.model;
//汉江师范学院 数计学院 吴乐创建于2024 4月 10 02:24

import lombok.Data;

@Data
public class UserMessage
{
    private String chatId;      //对话ID
    private String messageId;   //消息ID
    private String sender;      //发送者
    private String receiver;    //接收者
    /**
     * 消息类型
     * 0: 心跳包
     * 1：首次连接验证
     * 2：文本
     * 3：图片
     * 4：单视频
     * 5：单音频
     * 6：单文件
     * 7：文本，图片
     * 8：文本，单视频
     * 9：文本，文件
     * 10：文本，视频，图片
     * 20: 获取未读消息个数
     * 21：获取当前对话的消息
     * */
    private String type;        //消息类型
    private String text;        //文本消息内容
    private String video;       //视频消息名字
    private String[] image;       //图片消息名字
    private String audio;       //音频消息名字
    private String file;        //文件消息名字
    private String time;        //发送时间
    private String replyStatus; //回复状态,0为一般，1为回复
    private String replyMessageId; //回复消息ID
    private String authorization; //用户token
}