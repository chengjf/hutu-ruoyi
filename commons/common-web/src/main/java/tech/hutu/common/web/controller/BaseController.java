package tech.hutu.common.web.controller;

import com.alibaba.cola.exception.ExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.hutu.common.web.model.LoginUser;

/**
 * @author chengjf
 * @date 2024-01-09
 * @since 2024-01-09
 */
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw ExceptionFactory.bizException("获取用户信息异常");
        }
    }

    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw ExceptionFactory.bizException("获取用户信息异常");
        }
    }

}
