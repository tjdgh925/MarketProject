package com.project.market.api.profile.validator;

import com.project.market.api.profile.dto.ProfileUpdateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class ProfileUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProfileUpdateDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileUpdateDto profileUpdateDto = (ProfileUpdateDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "memberName", "required", "이름은 필수 입력 정보입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "required", "주소는 필수 입력 정보입니다.");
    }
}
