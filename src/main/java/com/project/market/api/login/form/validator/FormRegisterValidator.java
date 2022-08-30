package com.project.market.api.login.form.validator;

import com.project.market.api.login.form.dto.FormRegisterDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormRegisterValidator implements Validator {

    private static final String emailRegExp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern;

    private final int MIN_PASSWORD_LENGTH = 7;
    private final int MAX_PASSWORD_LENGTH = 16;

    public FormRegisterValidator() {
        pattern = Pattern.compile(emailRegExp);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FormRegisterDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FormRegisterDto formRequestDto = (FormRegisterDto) target;

        validateEmail(errors, formRequestDto);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "memberName", "required", "이름은 필수 입력 정보입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "required", "주소는 필수 입력 정보입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required", "비밀번호는 필수 입력 정보입니다.");
        validatePassword(errors, formRequestDto);
    }

    private void validatePassword(Errors errors, FormRegisterDto formRequestDto) {
        if (validatePasswordLength(formRequestDto)) {
            errors.rejectValue("password", "bad", "비밀번호는 8자 이상, 16자 이하로 입력해주세요.");
        }
        if (validatePasswordConfirm(formRequestDto)) {
            errors.rejectValue("passwordConfirm", "noMatch", "비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateEmail(Errors errors, FormRegisterDto formRequestDto) {
        if (isEmailNull(formRequestDto)) {
            errors.rejectValue("email", "required", "이메일은 필수 입력 정보입니다.");
            return;
        }

        Matcher matcher = pattern.matcher(formRequestDto.getEmail());
        if (!matcher.matches()) {
            errors.rejectValue("email", "bad", "올바르지 않은 이메일 형식입니다.");
        }

    }

    private boolean isEmailNull(FormRegisterDto formRequestDto) {
        return formRequestDto.getEmail() == null || formRequestDto.getEmail().trim().isEmpty();
    }

    private boolean validatePasswordLength(FormRegisterDto formRequestDto) {
        return formRequestDto.getPassword().length() <= MIN_PASSWORD_LENGTH
                || formRequestDto.getPassword().length() > MAX_PASSWORD_LENGTH
                && !StringUtils.isEmpty(formRequestDto.getPassword());
    }

    private boolean validatePasswordConfirm(FormRegisterDto formRequestDto) {
        return !formRequestDto.getPassword().equals(formRequestDto.getPasswordConfirm())
                && !StringUtils.isEmpty(formRequestDto.getPassword());
    }
}