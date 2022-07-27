package com.project.market.web.adminItem.controller;

import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.adminItem.dto.RegisterAdminItemDto;
import com.project.market.web.adminItem.dto.UpdateAdminItemDto;
import com.project.market.web.adminItem.service.AdminItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
        model.addAttribute("registerAdminItemDto", new RegisterAdminItemDto());
        return "adminitem/registeritemform";
    }

    @PostMapping(value = "/new")
    public String itemNew(
            Principal principal,
            @Valid @ModelAttribute RegisterAdminItemDto registerAdminItemDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        Long itemId;

        if (registerAdminItemDto.getItemImageFiles().get(0).isEmpty()) {
            bindingResult.reject("errorMessage", ErrorCode.NO_REP_IMAGE.getMessage());
            return "adminitem/registeritemform";
        } else if (bindingResult.hasErrors()) {
            return "adminitem/registeritemform";
        }

        try {
            itemId = adminItemService.addNewAdminItem(registerAdminItemDto, principal);
            redirectAttributes.addAttribute(("itemId"), itemId);
        } catch (Exception e) {
            bindingResult.reject("errorMessage", ErrorCode.ADD_ITEM_ERROR.getMessage());
            return "adminitem/registeritemform";
        }

        return "redirect:/admin/items/{itemId}";
    }


    @GetMapping("/{itemId}")
    public String itemEdit(
            @PathVariable Long itemId,
            Model model
    ) {
        UpdateAdminItemDto updateAdminItemDto = adminItemService.getItemAndImages(itemId);
        model.addAttribute("updateAdminItemDto", updateAdminItemDto);
        return "adminitem/updateitemform";
    }

    @PostMapping("/{itemId}")
    public String updateItem(
            @PathVariable Long itemId,
            @ModelAttribute UpdateAdminItemDto updateAdminItemDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (updateAdminItemDto.getItemImageFiles().get(0).isEmpty() &&
                !StringUtils.hasText(updateAdminItemDto.getOriginalImageNames().get(0))) {
            bindingResult.reject("errorMessage", ErrorCode.NO_REP_IMAGE.getMessage());
            updateAdminItemDto.setItemImageDtos(adminItemService.getItemAndImages(itemId).getItemImageDtos());
            return "adminitem/updateitemform";
        } else if (bindingResult.hasErrors()) {
            updateAdminItemDto.setItemImageDtos(adminItemService.getItemAndImages(itemId).getItemImageDtos());
            return "adminitem/updateitemform";
        }

        try {
            adminItemService.updateItem(updateAdminItemDto);
        } catch (Exception e) {
            bindingResult.reject("errorMessage", ErrorCode.UPDATE_ITEM_ERROR.getMessage());
            return "adminitem/updateitemform";
        }

        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/admin/items/{itemId}";
    }

}
