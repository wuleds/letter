package cn.wule.letter.user.vo;
//汉江师范学院 数计学院 吴乐创建于2023/12/14 0:33

/**
 * 对联系方法进行处理
 */
public abstract class Contact
{
    public String EMAIL = null;
    public String PHONE = null;

    public Contact(String contact, String method)
    {
        switch (method){
            case "email" -> EMAIL = contact;
            case "phone" -> PHONE = contact;
        }
    }
}