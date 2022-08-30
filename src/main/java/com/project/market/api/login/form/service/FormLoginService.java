package com.project.market.api.login.form.service;

import com.project.market.api.login.form.dto.FormRegisterDto;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormLoginService {

    private final MemberService memberService;

    @Transactional
    public Member registerMember(FormRegisterDto formRegisterDto) {
        Member newMember = formRegisterDto.toEntity();
        memberService.register(newMember);
        return newMember;
    }

}
