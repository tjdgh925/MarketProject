package com.project.market.domain.redis.service;

import com.project.market.domain.member.entity.Member;
import com.project.market.domain.redis.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    @Value("${token.refresh-token-expiration-time:1210500000}")
    private Long refreshTokenExpirationTime;

    private final RedisRepository redisRepository;

    @Transactional(readOnly = true)
    public String getRefreshTokenByEmail(String email) {
        return redisRepository.getValues(email);
    }

    @Transactional
    public void saveRefreshToken(Member member, String refreshToken){
        redisRepository.setValues(member.getEmail(), refreshToken, Duration.ofMillis(refreshTokenExpirationTime));
    }
}
