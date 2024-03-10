package cn.wule.letter.user.vo;
//汉江师范学院 数计学院 吴乐创建于2023/12/14 0:33

/**
 * 对联系方法进行处理
 */
public abstract class ContactWay
{
    public String EMAIL = null;
    public String PHONE = null;

    /**
     *
     * @param s 联系号码
     * @param method 联系方式，邮件或电话号码
     */
    public ContactWay(String s, String method)
    {
        switch (method){
            case "email" -> EMAIL = s;
            case "phone" -> PHONE = s;
        }
    }
}