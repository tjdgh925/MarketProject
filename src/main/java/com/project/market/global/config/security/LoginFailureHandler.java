package com.project.market.global.config.security;

import com.project.market.global.error.exception.ErrorCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String DEFAULT_FAILURE_URL = "/login?error=true";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
            errorMessage = ErrorCode.LOGIN_ERROR.getMessage();
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "존재하지 않는 아이디 입니다.";
        } else {
            errorMessage = "일 수 없는 이유로 로그인이 되지 않습니다.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");//오류 메시지가 깨져서 나오는 현상 방지
        setDefaultFailureUrl(DEFAULT_FAILURE_URL + "&exception=" + errorMessage);
        super.onAuthenticationFailure(request,response,exception);
    }
}
