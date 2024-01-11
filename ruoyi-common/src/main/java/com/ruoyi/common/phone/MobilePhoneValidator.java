package com.ruoyi.common.phone;

import com.ruoyi.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义手机号码校验注解实现
 *
 * @author ruoyi
 */
public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, String> {
    private static final String MOBILE_PHONE_PATTERN = "(?:0|86|\\+86)?1[3-9]\\d{9}";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        return !containsHtml(value);
    }

    public static boolean containsHtml(String value) {
        Pattern pattern = Pattern.compile(MOBILE_PHONE_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}