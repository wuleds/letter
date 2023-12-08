package cn.wule.letter.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * UUIDUtil类提供了生成UUID的方法。
 * UUID是基于强随机数生成器生成的，如果强随机数生成器不可用，将使用默认的随机数生成器。
 * 生成的UUID是一个字符串，其中的"-"已被删除。
 */
public class UUIDUtil {
    /**
     * 生成一个没有"-"的UUID字符串
     * @return 生成的UUID字符串
     */
    public static String getUUID() {
        SecureRandom secureRandom;
        try {
            // 尝试获取强随机数生成器
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            // 如果强随机数生成器不可用，使用默认的随机数生成器
            secureRandom = new SecureRandom();
        }
        // 使用随机数生成器生成UUID
        UUID uuid = new UUID(secureRandom.nextLong(), secureRandom.nextLong());
        // 返回没有"-"的UUID字符串
        return uuid.toString().replace("-", "");
    }
}