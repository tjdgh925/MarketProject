package com.project.market.domain.member.service;

import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService target;

    @Mock
    private MemberRepository memberRepository;

    private final String email = "test@email.com";
    private final Member member = Member.builder()
            .email("test@email.com")
            .memberName("tester")
            .address("서울특별시")
            .password("password")
            .memberType(MemberType.EMAIL)
            .role(MemberRole.USER)
            .build();


    @Test
    public void 회원가입실패_이미존재하는회원() throws Exception {
        //given
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        //when
       final BusinessException result = assertThrows(BusinessException.class, () -> target.register(member));


        //then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.ALREADY_REGISTERED_MEMBER.getMessage());
    }

    @Test
    public void 회원가입성공테스트() throws Exception {
        //given

        //when
        target.register(member);

        //then
        verify(memberRepository, times(1)).findByEmail(email);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    public void 회원조회테스트_실패() throws Exception {
        //given
        doReturn(Optional.empty()).when(memberRepository).findByEmail(email);

        //when
        final BusinessException result = assertThrows(BusinessException.class, () -> target.findByEmail(email));

        //then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.NO_MATCHING_MEMBER.getMessage());
    }

    @Test
    public void 회원조회테스트_성공() throws Exception {
        //given
        doReturn(Optional.of(member)).when(memberRepository).findByEmail(email);

        //when
        Optional<Member> result = target.findByEmail(email);

        //then
        assertThat(result.get()).isEqualTo(member);
    }

}