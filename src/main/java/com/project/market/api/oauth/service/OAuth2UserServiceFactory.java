package com.project.market.api.oauth.service;

import com.project.market.api.oauth.dto.GoogleOAuth2UserInfo;
import com.project.market.api.oauth.dto.KakaoOAuth2UserInfo;
import com.project.market.api.oauth.dto.OAuth2UserInfo;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;

import java.util.Map;

public class OAuth2UserServiceFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(MemberType memberType, Map<String, Object> attributes) {
        switch (memberType) {
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            default: throw new BusinessException(ErrorCode.NOT_VALID_MEMBER_TYPE);
        }
    }
}
