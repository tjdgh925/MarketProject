package com.project.market.api.login.token.controller;

import com.project.market.api.login.token.service.TokenService;
import com.project.market.global.config.security.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/renew")
    public ResponseEntity<TokenDto> refreshToken(@RequestHeader String Authorization, @RequestHeader String refreshToken) {
        return ResponseEntity.ok(tokenService.refreshToken(Authorization, refreshToken));
    }
}
