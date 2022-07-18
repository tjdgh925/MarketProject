package com.project.market.api.oauth.service;

import com.project.market.api.oauth.dto.OAuth2UserInfo;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.global.config.security.UserDetailsImpl;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return saveOrUpdate(userRequest, user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.NOT_VALID_MEMBER_TYPE);
        }
    }

    private OAuth2User saveOrUpdate(OAuth2UserRequest userRequest, OAuth2User user) {

        MemberType memberType = MemberType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserServiceFactory.getOAuth2UserInfo(memberType, user.getAttributes());
        Member savedUser = memberRepository.findByEmail(userInfo.getEmail())
                .orElse(userInfo.toEntity());

        memberRepository.save(savedUser);
        return UserDetailsImpl.create(savedUser, user.getAttributes());
    }
}
