package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/12 03:11

import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

/**
 * 邮件工具类
 */
@Component
@Slf4j
public class EmailUtil
{
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private JavaMailSender mailSender;

    /**
     * 发送验证码邮件
     * @param to 收件人
     * @param code 验证码
     */
    public void sendAuthEmail(String to, String code){
        String subject = "[验证码]来自Letter的验证码";
        String context = "你的验证码是：" + code + "，请在5分钟内完成验证。";
        sendEmail(to, subject, context, null, null);
    }

    /**
     * 发送邮件
     * @param to 收件人
     * @param subject 主题
     * @param context 内容
     * @param fileName 附件名
     * @param filePath 附件路径
     */
    public void sendEmail(String to, String subject, String context, String fileName, String filePath){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(context);
            //上传文件
            if(fileName != null && filePath != null) {
                helper.addAttachment(fileName, new File(filePath));
            }
            mailSender.send(mimeMessage);
            log.info("发送邮件，收件人：{}，主题：{}，内容：{}，附件名：{}，附件路径：{}", to, subject, context, fileName, filePath);
        }catch (Exception e){
            log.error("发送邮件失败", e);
        }
    }

    public void sendTemplateMail(String to, String subject, String templatePath, String... arguments) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(this.buildTemplateContext(templatePath, arguments), true);
        mailSender.send(mimeMessage);
    }

    private String buildTemplateContext(String templatePath, String... arguments) {
        //加载邮件html模板
        org.springframework.core.io.Resource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            inputStream = resource.getInputStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.info("读取模板失败:", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    log.error("关闭文件流失败:{}", e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭文件流失败:{}", e.getMessage());
                }
            }
        }
        //替换html模板中的参数
        return MessageFormat.format(buffer.toString(), arguments);
    }
}