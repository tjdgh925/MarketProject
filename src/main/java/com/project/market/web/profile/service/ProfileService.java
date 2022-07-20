package com.project.market.web.profile.service;

import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.profile.dto.ProfileUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MemberService memberService;

    public Member getMemberInfo(String email) {
        return memberService.findByEmail(email)
                .orElseThrow(()-> new BusinessException(ErrorCode.NO_MATCHING_MEMBER));
    }

    @Transactional
    public Member updateMemberInfo(String email, ProfileUpdateDto profileUpdateDto) {
        String address = profileUpdateDto.getAddress();
        String memberName = profileUpdateDto.getName();
        return memberService.update(email, address, memberName);
    }

}
