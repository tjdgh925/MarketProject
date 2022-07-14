package com.project.market.web.register.controller;

import com.project.market.global.error.exception.BusinessException;
import com.project.market.web.register.dto.MemberRegisterDto;
import com.project.market.web.register.service.MemberRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberRegisterController {

    private final MemberRegisterService memberRegisterService;

    @GetMapping("/register")
    public String getRegisterView(Model model) {
        model.addAttribute("memberRegisterDto", new MemberRegisterDto());
        return "register/registerform";
    }

    @PostMapping("/register")
    public String addMember(@Valid @ModelAttribute MemberRegisterDto memberRegisterDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register/registerform";
        }

        try {
            memberRegisterService.register(memberRegisterDto);
        } catch (BusinessException e) {
            e.printStackTrace();
            bindingResult.reject("errorMessage", e.getMessage());
            return "register/registerform";
        }

        return "redirect:/login";
    }
}
