package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.service.MemberService;

import java.util.List;
import java.util.Optional;

public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    public Long createMember(Member member) {
        return memberService.join(member);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberService.findOne(id);
    }

    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }

    public boolean deleteMember(Long id) {
        return memberService.deleteMember(id);
    }
}