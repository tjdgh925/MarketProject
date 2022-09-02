package com.project.market.api.login.form.validator;

import com.project.market.api.login.form.dto.FormLoginRequestDto;
import com.project.market.api.login.form.dto.FormRegisterDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormLoginValidator implements Validator {

    private static final String emailRegExp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern;

    public FormLoginValidator() {
        pattern = Pattern.compile(emailRegExp);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FormLoginRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FormLoginRequestDto requestDto = (FormLoginRequestDto) target;

        validateEmail(errors, requestDto);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required", "비밀번호는 필수 입력 정보입니다.");
    }

    private void validateEmail(Errors errors, FormLoginRequestDto formRequestDto) {
        if (isEmailNull(formRequestDto)) {
            errors.rejectValue("email", "required", "이메일은 필수 입력 정보입니다.");
            return;
        }

        Matcher matcher = pattern.matcher(formRequestDto.getEmail());
        if (!matcher.matches()) {
            errors.rejectValue("email", "bad", "올바르지 않은 이메일 형식입니다.");
        }
    }

    private boolean isEmailNull(FormLoginRequestDto formRequestDto) {
        return formRequestDto.getEmail() == null || formRequestDto.getEmail().trim().isEmpty();
    }
}
