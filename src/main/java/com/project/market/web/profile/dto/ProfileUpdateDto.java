package com.project.market.web.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateDto {

    private String name;

    private String address;

    @Builder
    public ProfileUpdateDto(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
