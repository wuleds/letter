package cn.wule.letter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class LetterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LetterApplication.class, args);
    }
}