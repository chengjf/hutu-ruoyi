package tech.hutu.ruoyi;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class RuoyiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuoyiApplication.class, args);
    }

}
