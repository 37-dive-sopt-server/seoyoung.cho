package org.sopt.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.member.domain.Member;
import org.sopt.domain.member.dto.MemberCreateRequest;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.domain.member.service.validator.MemberValidator;
import org.sopt.global.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    @Override
    @Transactional
    public Member join(MemberCreateRequest request) {
        Member member = Member.createLocalMember(
                request.name(),
                request.password(),
                request.birthdate(),
                request.email(),
                request.gender()
        );

        memberValidator.validateNewMember(member); // 검증 로직 Validator 위임
        return memberRepository.save(member);
    }

    @Override
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = this.findOne(memberId);
        memberRepository.deleteById(member.getId());
    }

    @Override
    public Page<Member> findAllMembers(Pageable pageable) {
        log.info("회원 목록 조회 (페이지네이션) - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return memberRepository.findAll(pageable);
    }
}
