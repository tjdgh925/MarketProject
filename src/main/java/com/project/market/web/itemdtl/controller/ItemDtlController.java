package com.project.market.web.itemdtl.controller;

import com.project.market.web.itemdtl.dto.ItemDtlDto;
import com.project.market.web.itemdtl.service.ItemDtlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/itemdtl")
public class ItemDtlController {

    private final ItemDtlService itemDtlService;

    @GetMapping("/{itemId}")
    public String getItemDetail(Model model, @PathVariable Long itemId) {
        ItemDtlDto item = itemDtlService.getItemDtl(itemId);
        model.addAttribute("item", item);

        return "itemdtl/itemdtl";
    }
}
