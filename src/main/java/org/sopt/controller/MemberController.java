package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.dto.MemberCreateRequest;
import org.sopt.dto.MemberResponse;
import org.sopt.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<String> createMember(@RequestBody MemberCreateRequest request) {
        Long memberId = memberService.join(request);
        return ResponseEntity.created(URI.create("/members/" + memberId))
                .body("✅ 회원 등록 완료 (ID: \" + memberId + \")");
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findMemberById(@PathVariable Long id) {
        Optional<Member> memberOpt = memberService.findOne(id);

        return memberOpt
                .map(member -> ResponseEntity.ok(MemberResponse.of(member)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();

        List<MemberResponse> responses = members.stream()
                .map(MemberResponse::of)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        boolean isDeleted = memberService.deleteMember(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 성공 시 204
        } else {
            return ResponseEntity.notFound().build(); // 실패 시 404
        }
    }
}