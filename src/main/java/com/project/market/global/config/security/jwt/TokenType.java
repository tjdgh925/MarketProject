package com.project.market.global.config.security.jwt;

public enum TokenType {

    ACCESS, REFRESH;

    public static boolean isAccessToken(String tokenType) {
        return TokenType.ACCESS.name().equals(tokenType);
    }
}
