package com.project.market.api.login.form.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FormLoginRequestDto {
    private String email;
    private String password;
}
