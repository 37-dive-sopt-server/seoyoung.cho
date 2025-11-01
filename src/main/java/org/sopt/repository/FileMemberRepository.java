package org.sopt.repository;

import jakarta.annotation.PreDestroy;
import org.sopt.service.component.IdGenerator;
import org.sopt.domain.Member;
import org.sopt.repository.storage.MemberFileStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Qualifier("fileRepo")
public class FileMemberRepository implements MemberRepository {

    private final Map<Long, Member> store;
    private final MemberFileStorage fileStorage;
    private final IdGenerator idGenerator;

    public FileMemberRepository(MemberFileStorage fileStorage, IdGenerator idGenerator) {
        this.fileStorage = fileStorage;
        this.idGenerator = idGenerator;

        this.store = fileStorage.loadFromFile();
        initializeSequence();
    }

    private void initializeSequence() {
        long maxId = store.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);

        idGenerator.initialize(maxId);
    }

    // 메모리만 수정
    @Override
    public Member save(Member member) {
        Long newId = idGenerator.generate();
        Member savedMember = new Member(
                newId, member.getName(), member.getBirthdate(),
                member.getEmail(), member.getGender());

        store.put(savedMember.getId(), savedMember);
        return savedMember;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @PreDestroy
    public void flushDataToFile() {
        System.out.println("프로그램 종료... 모든 데이터를 파일에 영속화합니다.");
        fileStorage.saveToFile(store);
        System.out.println("데이터 저장 완료.");
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return store.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

}