package com.project.market.domain.member.repository;

import com.project.market.TestConfig;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.global.config.jpa.AuditConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AuditConfig.class, TestConfig.class})
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void MemberRepository가_NULL이_아님() {
        //given

        //when

        //then
        assertThat(memberRepository).isNotNull();
    }

    @Test
    public void 회원가입_테스트() throws Exception {
        //given
        final Member member = Member.builder()
                .email("test@email.com")
                .address("서울특별시")
                .memberName("tester")
                .password("password")
                .memberType(MemberType.EMAIL)
                .role(MemberRole.USER)
            .build();

        //when
        final Member result = memberRepository.save(member);

        //then
        assertThat(result.getEmail()).isEqualTo("test@email.com");
        assertThat(result.getMemberName()).isEqualTo("tester");
        assertThat(result.getAddress()).isEqualTo("서울특별시");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getMemberType()).isEqualTo(MemberType.EMAIL);
        assertThat(result.getRole()).isEqualTo(MemberRole.USER);
    }

    @Test
    public void FindMemberByEmail테스트() throws Exception {
        //given
        final Member member = Member.builder()
                .email("test@email.com")
                .address("서울특별시")
                .memberName("tester")
                .password("password")
                .memberType(MemberType.EMAIL)
                .role(MemberRole.USER)
                .build();

        //when
        memberRepository.save(member);
        Member result = memberRepository.findByEmail("test@email.com")
                .orElse(null);

        //then
        assertThat(result.getEmail()).isEqualTo("test@email.com");
        assertThat(result.getMemberName()).isEqualTo("tester");
        assertThat(result.getAddress()).isEqualTo("서울특별시");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getMemberType()).isEqualTo(MemberType.EMAIL);
        assertThat(result.getRole()).isEqualTo(MemberRole.USER);
    }

}