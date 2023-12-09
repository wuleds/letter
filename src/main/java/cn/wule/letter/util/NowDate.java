package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 2:18

import java.text.SimpleDateFormat;
import java.util.Date;

public class NowDate
{
    /**
     * 获取当前时间
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDate()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}