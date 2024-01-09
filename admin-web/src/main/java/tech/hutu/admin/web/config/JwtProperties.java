package tech.hutu.admin.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chengjf
 * @date 2024-01-09
 * @since 2024-01-09
 */
@ConfigurationProperties(prefix = "jwt")
@Data
@Configuration
public class JwtProperties {

    private String secretKey = "flzxsqcysyhljt";
    //validity in milliseconds
    private long validityInMs = 3600000; // 1h
}