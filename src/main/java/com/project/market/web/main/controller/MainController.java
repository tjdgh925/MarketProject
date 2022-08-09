package com.project.market.web.main.controller;

import com.project.market.web.main.dto.ItemSearchDto;
import com.project.market.web.main.dto.MainItemDto;
import com.project.market.web.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final int MIN_CONTENT = 0;
    private final int MAX_CONTENT = 6;
    private final int MAX_PAGE = 5;


    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : MIN_CONTENT, MAX_CONTENT);

        Page<MainItemDto> items = mainService.getMainItem(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", MAX_PAGE);

        return "main/mainpage";
    }

}
