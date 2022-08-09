package com.project.market.web.itemdtl.controller;

import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.itemdtl.dto.ItemDtlDto;
import com.project.market.web.itemdtl.dto.RegisterOrderDto;
import com.project.market.web.itemdtl.service.ItemDtlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    @PostMapping("/order")
    public @ResponseBody ResponseEntity registerOrderItem(@RequestBody RegisterOrderDto registerOrderDto, Principal principal) {
        try {
            itemDtlService.registerOrderItem(registerOrderDto, principal);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(ErrorCode.NOT_ENOUGH_STOCK.getStatus()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/cart")
    public @ResponseBody ResponseEntity cartOrderItem(@RequestBody RegisterOrderDto registerOrderDto, Principal principal) {
        try {
            itemDtlService.cartOrderItem(registerOrderDto, principal);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(ErrorCode.NOT_ENOUGH_STOCK.getStatus()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
