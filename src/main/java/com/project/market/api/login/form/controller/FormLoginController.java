package com.project.market.api.login.form.controller;

import com.project.market.api.login.form.dto.FormRegisterDto;
import com.project.market.api.login.form.service.FormLoginService;
import com.project.market.api.login.form.validator.FormRegisterValidator;
import com.project.market.domain.member.entity.Member;
import com.project.market.global.error.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FormLoginController {

    private final FormLoginService formLoginService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@Valid @RequestBody FormRegisterDto formRegisterDto, Errors errors) {
        new FormRegisterValidator().validate(formRegisterDto, errors);

        if (errors.hasErrors()) {
            InvalidParameterException.throwErrorMessage(errors);
        }

        Member member = formLoginService.registerMember(formRegisterDto);

        String result = member.getMemberName() + "님 회원가입에 성공했습니다.";

        return ResponseEntity.ok(result);
    }
}
