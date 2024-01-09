package tech.hutu.admin.web.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author chengjf
 * @date 2024-01-09
 * @since 2024-01-09
 */
@Data
public class AuthReq {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
