package com.project.market.domain.member.entity;

import com.project.market.domain.base.BaseTimeEntity;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String address;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String refreshToken;
    private String tokenExpirationTime;

    @Builder
    public Member(String email, String memberName,String address, MemberType memberType, String password, MemberRole role) {
        this.email = email;
        this.address = address;
        this.memberName = memberName;
        this.memberType = memberType;
        this.password = password;
        this.role = role;
    }

    public void updateInfo(String address, String memberName) {
        this.address = address;
        this.memberName = memberName;
    }
}
