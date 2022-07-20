package com.project.market.web.profile.controller;

import com.project.market.domain.member.entity.Member;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.web.profile.dto.ProfileUpdateDto;
import com.project.market.web.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public String getProfileView(Model model, Principal principal) {
        Member member = profileService.getMemberInfo(principal.getName());
        model.addAttribute("profileUpdateDto",
                ProfileUpdateDto.builder()
                        .name(member.getMemberName())
                        .address(member.getAddress())
                        .build());
        return "/profile/profileform";
    }

    @PostMapping ("/profile")
    public String changeMemberInfo(
            Principal principal,
            @Valid @ModelAttribute ProfileUpdateDto profileUpdateDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "profile/profileform";
        }

        try{
            profileService.updateMemberInfo(principal.getName(), profileUpdateDto);
        } catch (BusinessException e) {
            e.printStackTrace();
            bindingResult.reject("errorMessage", e.getMessage());
            return "register/registerform";
        }

        return "redirect:/";
    }
}
