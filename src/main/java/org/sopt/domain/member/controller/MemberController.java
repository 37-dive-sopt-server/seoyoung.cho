package org.sopt.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.domain.member.domain.Member;
import org.sopt.domain.member.dto.MemberCreateRequest;
import org.sopt.domain.member.dto.MemberDeleteResponse;
import org.sopt.domain.member.dto.MemberListResponse;
import org.sopt.domain.member.dto.MemberResponse;
import org.sopt.domain.member.service.MemberService;
import org.sopt.global.dto.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Tag(name = "Members", description = "회원 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberCreateRequest request) {
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

    // 페이지네이션 지원 API
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<Member>>> getAllMembersWithPagination(
            @PageableDefault(size = 10, sort = "id", direction = DESC) Pageable pageable
    ) {
        Page<Member> members = memberService.findAllMembers(pageable);
        return ResponseEntity.ok(ApiResponse.ok(members));
    }
}