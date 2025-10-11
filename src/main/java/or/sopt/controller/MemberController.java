package or.sopt.controller;

import or.sopt.domain.Member;
import or.sopt.service.MemberServiceImpl;

import java.util.List;
import java.util.Optional;

public class MemberController {

    private MemberServiceImpl memberService = new MemberServiceImpl();

    public Long createMember(String name) {
        return memberService.join(name);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberService.findOne(id);
    }

    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }
}