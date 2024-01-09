package tech.hutu.admin.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author chengjf
 * @date 2024-01-09
 * @since 2024-01-09
 */
@SpringBootApplication(scanBasePackages = {"tech.hutu"})
@EnableDubbo
public class AdminWebApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AdminWebApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }
}
