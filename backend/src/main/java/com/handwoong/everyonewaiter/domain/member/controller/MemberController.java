package com.handwoong.everyonewaiter.domain.member.controller;

import com.handwoong.everyonewaiter.domain.member.dto.MemberRegisterRequest;
import com.handwoong.everyonewaiter.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid final MemberRegisterRequest request) {
        memberService.register(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
