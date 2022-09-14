package com.project.market.api.profile.service;

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

    public String getEmailByToken(String bearerToken) {
        String accessToken = getJwtToken(bearerToken);

        return tokenProvider.getEmail(accessToken);
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
