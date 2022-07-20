package com.project.market.web.profile.service;

import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.profile.dto.ProfileUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @InjectMocks
    private ProfileService target;
    @Mock
    private MemberService memberService;

    private final String email = "test@email.com";


    @Test
    public void 회원정보수정테스트_실패() throws Exception {
        //given
        doThrow(new BusinessException(ErrorCode.NO_MATCHING_MEMBER))
                .when(memberService).update(email, "address", "name");
        //when
        final BusinessException result = assertThrows(BusinessException.class, () ->
                target.updateMemberInfo(email,
                        ProfileUpdateDto.builder()
                                .address("address")
                                .name("name")
                                .build()
                )
        );

        //then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.NO_MATCHING_MEMBER.getMessage());
    }

    @Test
    public void 회원정보수정테스트_성공() throws Exception {
        //given
        final Member member = Member.builder()
                .email("test@email.com")
                .memberName("name")
                .address("address")
                .password("password")
                .memberType(MemberType.EMAIL)
                .role(MemberRole.USER)
                .build();

        doReturn(member).when(memberService).update(email, "address", "name");

        //when
        Member result = target.updateMemberInfo(email, ProfileUpdateDto.builder()
                .address("address")
                .name("name")
                .build());

        //then
        assertThat(result.getAddress()).isEqualTo("address");
        assertThat(result.getMemberName()).isEqualTo("name");
    }
}