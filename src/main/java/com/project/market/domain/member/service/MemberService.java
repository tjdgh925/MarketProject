package com.project.market.domain.member.service;

import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.domain.redis.repository.RedisRepository;
import com.project.market.domain.redis.service.RedisService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.EntityNotFoundException;
import com.project.market.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    
    @Transactional
    public void register(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());
        if (optionalMember.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_MEMBER);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Member findMember(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NO_MATCHING_MEMBER));
    }

    @Transactional
    public Member update(String email, String address, String memberName) {
        Member member = findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_MATCHING_MEMBER));

        member.updateInfo(address, memberName);

        return member;
    }

    public void saveRefreshToken(Member member, String refreshToken) {
        redisService.saveRefreshToken(member, refreshToken);
    }

    public String getRefreshTokenByEmail(String email) {
        String refreshToken = redisService.getRefreshTokenByEmail(email);
        return refreshToken;
    }
}
