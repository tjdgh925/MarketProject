package com.project.market.api.profile.controller;

import com.project.market.api.profile.dto.ProfileUpdateDto;
import com.project.market.api.profile.service.ProfileApiService;
import com.project.market.api.profile.validator.ProfileUpdateValidator;
import com.project.market.global.error.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileApiController {

    private final ProfileApiService profileApiService;
    private final ProfileUpdateValidator profileUpdateValidator;

    private final String AUTHORIZATION = "Authorization";

    @GetMapping("/email")
    public ResponseEntity getEmail(@RequestHeader(AUTHORIZATION) String bearerToken) {
        return ResponseEntity.ok(profileApiService.getEmailByToken(bearerToken));
    }

    @GetMapping("/profile")
    public ResponseEntity getProfile(@RequestHeader(AUTHORIZATION) String bearerToken) {
        return ResponseEntity.ok(profileApiService.getMemberByToken(bearerToken));
    }

    @PatchMapping("/profile")
    public ResponseEntity updateProfile(
            @RequestHeader(AUTHORIZATION) String bearerToken,
            @Valid @RequestBody ProfileUpdateDto profileUpdateDto,
            Errors errors
    ) {
        profileUpdateValidator.validate(profileUpdateDto, errors);

        profileApiService.updateProfile(bearerToken, profileUpdateDto);

        if (errors.hasErrors()) {
            InvalidParameterException.throwErrorMessage(errors);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
