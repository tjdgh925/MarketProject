package com.project.market.api.profile.service;

import com.project.market.api.profile.dto.ProfileResponseDto;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.config.security.jwt.JwtTokenProvider;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.global.error.exception.TokenException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileApiService {

    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    public String getEmailByToken(String bearerToken) {
        String accessToken = getJwtToken(bearerToken);

        return tokenProvider.getEmail(accessToken);
    }
    public ProfileResponseDto getMemberByToken(String bearerToken) {
        String accessToken = getJwtToken(bearerToken);
        Member member = getMember(accessToken);

        return ProfileResponseDto.of(member);
    }

    private Member getMember(String accessToken) {
        String email = tokenProvider.getEmail(accessToken);
        Member member = memberService.findMember(email);
        return member;
    }


    private String getJwtToken(String bearerToken) {
        if (!validateBearerToken(bearerToken)) {
            throw new TokenException(ErrorCode.NOT_VALID_TOKEN.getMessage());
        }
        return bearerToken.substring("Bearer ".length());
    }

    private boolean validateBearerToken(String bearerToken) {
        return StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ");
    }

}
