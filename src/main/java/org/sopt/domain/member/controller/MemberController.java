package org.sopt.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberCreateRequest request) {
        Member newMember = memberService.join(request);
        MemberResponse response = MemberResponse.from(newMember);

        return ResponseEntity
                .status(HttpStatus.CREATED) // 201
                .body(ApiResponse.created(response));
    }

    @Operation(summary = "회원 조회", description = "특정 회원의 정보를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<MemberResponse>> findMemberById(@PathVariable Long userId) {
        Member member = memberService.findOne(userId);
        MemberResponse response = MemberResponse.from(member);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "회원 목록 조회", description = "모든 회원 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<MemberListResponse>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        MemberListResponse response = MemberListResponse.from(members);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "회원 삭제", description = "특정 회원을 삭제합니다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<MemberDeleteResponse>> deleteMember(@PathVariable Long userId) {
        memberService.deleteMember(userId);
        MemberDeleteResponse response = MemberDeleteResponse.of(userId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "회원 목록 조회 (페이지네이션)", description = "회원 목록을 페이지네이션하여 조회합니다.")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<Member>>> getAllMembersWithPagination(
            @PageableDefault(size = 10, sort = "id", direction = DESC) Pageable pageable
    ) {
        Page<Member> members = memberService.findAllMembers(pageable);
        return ResponseEntity.ok(ApiResponse.ok(members));
    }
}