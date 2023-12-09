package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 0:12

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class UserUtil {
    private static final String CONFIG_FILE_PATH = "src/main/resources/userId.properties";
    private static AtomicLong id = new AtomicLong(10000);
    static {
        try {
            Properties props = new Properties();
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            props.load(fileInputStream);
            fileInputStream.close();
            Long currentId = Long.parseLong(props.getProperty("id", "100000"));
            id = new AtomicLong(currentId);
        } catch (IOException e) {
            log.error("Failed to load unique id from config file:{}", e.getMessage());
        }
    }

    public static synchronized long createUserId() {
        Lock lock = new ReentrantLock();
        try{
            if (lock.tryLock()) {
                try {
                    long currentId = id.getAndIncrement();
                    saveIdToConfig(currentId + 1);
                    return currentId;
                } finally {
                    lock.unlock();
                }
            }
        }catch (Exception e){
            log.error("获取锁失败",e);
        }
        return -1;
    }

    /**
     * 保存当前id到配置文件
     * @param currentId 当前id
     */
    private static void saveIdToConfig(long currentId) {
        try {
            Properties props = new Properties();
            File configFile = new File(CONFIG_FILE_PATH);

            if (configFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(configFile);
                props.load(fileInputStream);
                fileInputStream.close();
            }

            props.setProperty("id", String.valueOf(currentId));

            FileOutputStream fileOutputStream = new FileOutputStream(CONFIG_FILE_PATH);
            props.store(fileOutputStream, "Unique ID");
            fileOutputStream.close();
        } catch (IOException e) {
            log.error("Failed to save unique id to config file", e);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            long userId = createUserId();
            System.out.println(userId);
        }
    }
}