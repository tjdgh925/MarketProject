package com.project.market.web.adminItem.controller;

import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.adminItem.dto.AdminItemDto;
import com.project.market.web.adminItem.service.AdminItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/items")
public class AdminItemController {

    private final AdminItemService adminItemService;

    @GetMapping("/new")
    public String getAdminItemView(Model model) {
        model.addAttribute("adminItemDto", new AdminItemDto());
        return "adminitem/registeritemform";
    }

    @PostMapping(value = "/new")
    public String itemNew(
            Principal principal,
            @Valid @ModelAttribute AdminItemDto adminItemDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        Long itemId;

        if (adminItemDto.getItemImageFiles().get(0).isEmpty()) {
            bindingResult.reject("errorMessage", ErrorCode.NO_REP_IMAGE.getMessage());
            return "adminitem/registeritemform";
        } else if (bindingResult.hasErrors()) {
            return "adminitem/registeritemform";
        }

        try {
            itemId = adminItemService.addNewAdminItem(adminItemDto, principal);
            redirectAttributes.addAttribute(("itemId"), itemId);
        } catch (Exception e) {
            bindingResult.reject("errorMessage", ErrorCode.ADD_ITEM_ERROR.getMessage());
            return "adminitem/registeritemform";
        }

        return "redirect:/admin/items/{itemId}";
    }
}
