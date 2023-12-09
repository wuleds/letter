package cn.wule.letter.util;

import io.netty.util.internal.ConcurrentSet;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * UUIDUtil类提供了生成UUID的方法。
 * UUID是基于强随机数生成器生成的，如果强随机数生成器不可用，将使用默认的随机数生成器。
 * 生成的UUID是一个字符串，其中的"-"已被删除。
 */
@Slf4j
public class UUIDUtil {
    static Set<String> set = new CopyOnWriteArraySet<>();
    public static String getUUID() {
        Lock lock = new ReentrantLock();
        try {
            if (lock.tryLock(10, TimeUnit.MILLISECONDS)) {
                try {
                    String uuid = createUUID();
                    //如果uuid已经存在则重新生成
                    int i = 1;
                    while (!set.add(uuid)) {
                        uuid = createUUID();
                        if (i++ > 10) {
                            break;
                        }
                    }
                    return uuid;
                } catch (Exception e) {
                    log.error("超时", e);
                } finally {
                    lock.unlock();
                }
            }
        }catch (Exception e){
            log.error("获取锁失败",e);
        }
        return null;
    }
    /**
     * 生成一个没有"-"的UUID字符串
     * @return 生成的UUID字符串
     */
    private static String createUUID() {
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