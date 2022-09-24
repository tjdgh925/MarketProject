package com.project.market.api.profile.dto;

import com.project.market.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ProfileResponseDto {

    private String email;
    private String memberName;
    private String address;

    public static ProfileResponseDto of(Member member) {
        return ProfileResponseDto.builder()
                .email(member.getEmail())
                .memberName(member.getMemberName())
                .address(member.getAddress())
                .build();
    }

}
