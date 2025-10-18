package org.sopt;

import org.sopt.controller.MemberController;
import org.sopt.repository.FileMemberRepository;
import org.sopt.repository.MemberRepository;
import org.sopt.service.MemberService;
import org.sopt.service.MemberServiceImpl;
import org.sopt.view.MemberView;

public class Main {
    public static void main(String[] args) {
        MemberRepository memberRepository = new FileMemberRepository();
        MemberService memberService = new MemberServiceImpl(memberRepository);
        MemberController memberController = new MemberController(memberService);
        MemberView memberView = new MemberView(memberController);
        
        // 프로그램 실행
        memberView.run();
    }
}