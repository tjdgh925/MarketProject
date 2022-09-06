package com.project.market.global.config.security;

import com.google.gson.Gson;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.config.security.jwt.JwtTokenProvider;
import com.project.market.global.config.security.jwt.TokenDto;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member member = memberService.findByEmail(authentication.getName())
                .orElseThrow(()-> new BusinessException(ErrorCode.NO_MATCHING_MEMBER));

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        TokenDto tokenDto = tokenProvider.createTokenDto(member);
        String url = makeRedirectUrl(tokenDto);

        getRedirectStrategy().sendRedirect(request, response, url);

    }
    private String makeRedirectUrl(TokenDto token) {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect/"+token.getAccessToken()+"+"+token.getRefreshToken())
                .build().toUriString();
    }
}
