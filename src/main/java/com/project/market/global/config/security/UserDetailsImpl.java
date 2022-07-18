package com.project.market.global.config.security;

import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.nio.file.attribute.UserPrincipal;
import java.util.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetailsImpl implements UserDetails, OAuth2User {

    private final String email;
    private final String password;
    private final MemberType memberType;
    private final MemberRole memberRole;
    private final Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return email;
    }

    public static UserDetailsImpl create(Member member) {
        return new UserDetailsImpl(
                member.getEmail(),
                member.getPassword(),
                member.getMemberType(),
                MemberRole.USER,
                Collections.singletonList(new SimpleGrantedAuthority(MemberRole.USER.getKey()))
        );
    }

    public static UserDetailsImpl create(Member member, Map<String, Object> attributes) {
        UserDetailsImpl userPrincipal = create(member);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }
}
