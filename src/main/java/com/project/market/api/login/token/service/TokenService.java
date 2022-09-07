package com.project.market.api.login.token.service;

import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.config.security.jwt.JwtTokenProvider;
import com.project.market.global.config.security.jwt.TokenDto;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.global.error.exception.TokenException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    @Transactional
    public TokenDto refreshToken(String accessToken, String refreshToken) {
        accessToken = getJwtToken(accessToken);
        refreshToken = getJwtToken(refreshToken);

        if (tokenProvider.validateToken(accessToken)) {
            return TokenDto.builder().
                    accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        String email = tokenProvider.getEmail(refreshToken);
        String savedToken = memberService.getRefreshTokenByEmail(email);

        validateRefreshToken(refreshToken, savedToken);

        return createNewToken(email);
    }

    private TokenDto createNewToken(String email) {
        Member member = memberService.findMember(email);
        TokenDto tokenDto = tokenProvider.createTokenDto(member);
        memberService.saveRefreshToken(member, tokenDto.getRefreshToken());
        return tokenDto;
    }

    private boolean validateRefreshToken(String refreshToken, String savedToken) {
        if (!(tokenProvider.validateToken(refreshToken) && validateSavedToken(refreshToken, savedToken)))
            throw new TokenException(ErrorCode.NOT_VALID_TOKEN.getMessage());

        return true;
    }

    private boolean validateSavedToken(String refreshToken, String savedToken) {
        if (!StringUtils.equals(savedToken, refreshToken))
            throw new TokenException(ErrorCode.NOT_VALID_TOKEN.getMessage());

        return true;
    }

    private String getJwtToken(String bearerToken) {
        if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
}
