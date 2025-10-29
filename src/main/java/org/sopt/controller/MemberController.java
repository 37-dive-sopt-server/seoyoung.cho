package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.service.MemberService;
import org.springframework.web.bind.annotation.*;

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
    public Long createMember(@RequestBody Member member) {
        return memberService.join(member);
    }

    @GetMapping("/{id}")
    public Optional<Member> findMemberById(@PathVariable Long id) {
        return memberService.findOne(id);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }

    @DeleteMapping("/{id}")
    public boolean deleteMember(@PathVariable Long id) {
        return memberService.deleteMember(id);
    }
}