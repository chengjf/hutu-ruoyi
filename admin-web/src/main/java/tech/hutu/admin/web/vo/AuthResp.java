package tech.hutu.admin.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chengjf
 * @date 2024-01-09
 * @since 2024-01-09
 */
@Data
@Builder
public class AuthResp {

    private String username;

    private String token;

}
