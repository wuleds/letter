package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/16 17:39

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * 生成随机字符串
 */
@Component
public class RandomString
{
    /**返回一个128个字符的字符串*/
    public String getLongLink(){
        return generateRandomString(128);
    }

    public static String generateRandomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer");
        }

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length / 2];
        random.nextBytes(bytes);

        BigInteger bigInt = new BigInteger(1, bytes);
        StringBuilder randomString = new StringBuilder(bigInt.toString(16));

        // Ensure that the string has the desired length
        while (randomString.length() < length) {
            randomString.insert(0, "0");
        }

        return randomString.toString();
    }
}