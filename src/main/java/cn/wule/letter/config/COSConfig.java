package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 02:48

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class COSConfig
{
    @Value("${cos.secretId}")
    private String secretId;
    @Value("${cos.secretKey}")
    private String secretKey;
    @Value("${cos.region}")
    private String region;
    @Value("${cos.bucketName}")
    private String bucketName;
    @Value("${cos.url}")
    private String url;

    @Bean
    public COSClient cosClient(){
        COSCredentials cred = new BasicCOSCredentials(this.secretId, this.secretKey);
        Region region = new Region(this.region);
        ClientConfig config = new ClientConfig(region);

        COSClient cosClient = new COSClient(cred, config);
        return cosClient;
    }
}