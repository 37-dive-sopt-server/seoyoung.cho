package org.sopt.controller;

import org.sopt.dto.ApiResponse;
import org.sopt.domain.Member;
import org.sopt.dto.MemberCreateRequest;
import org.sopt.dto.MemberResponse;
import org.sopt.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@RequestBody MemberCreateRequest request) {
        Member newMember = memberService.join(request);
        MemberResponse response = MemberResponse.of(newMember);

        return ResponseEntity
                .created(URI.create("/members/" + newMember.getId()))
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "✅ 회원 등록 성공", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> findMemberById(@PathVariable Long id) {
        Member member = memberService.findOne(id);
        MemberResponse response = MemberResponse.of(member);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "✅ 회원 조회 성공", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        List<MemberResponse> responses = members.stream()
                .map(MemberResponse::of)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "✅ 전체 회원 조회 성공", responses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT) // 204
                .body(ApiResponse.success(HttpStatus.NO_CONTENT.value(), "✅ 회원 삭제 성공"));
    }
}