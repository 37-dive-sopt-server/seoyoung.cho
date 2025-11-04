package org.sopt.member.controller;

import org.sopt.dto.*;
import org.sopt.member.domain.Member;
import org.sopt.global.dto.ApiResponse;
import org.sopt.member.dto.MemberCreateRequest;
import org.sopt.member.dto.MemberDeleteResponse;
import org.sopt.member.dto.MemberListResponse;
import org.sopt.member.dto.MemberResponse;
import org.sopt.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@RequestBody MemberCreateRequest request) {
        Member newMember = memberService.join(request);
        MemberResponse response = MemberResponse.from(newMember);

        return ResponseEntity
                .status(HttpStatus.CREATED) // 201
                .body(ApiResponse.created(response));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<MemberResponse>> findMemberById(@PathVariable Long userId) {
        Member member = memberService.findOne(userId);
        MemberResponse response = MemberResponse.from(member);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MemberListResponse>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        MemberListResponse response = MemberListResponse.from(members);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<MemberDeleteResponse>> deleteMember(@PathVariable Long userId) {
        memberService.deleteMember(userId);
        MemberDeleteResponse response = MemberDeleteResponse.of(userId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}