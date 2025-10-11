package or.sopt.repository;

import or.sopt.domain.Member;

import java.util.*;

public class MemoryMemberRepository {

    private static final Map<Long, Member> store = new HashMap<>();


    public Member save(Member member) {

        store.put(member.getId(), member);
        return member;

    }


    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }


    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}
