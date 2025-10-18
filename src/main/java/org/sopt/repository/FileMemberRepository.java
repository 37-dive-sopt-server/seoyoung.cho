package org.sopt.repository;

import org.sopt.domain.Gender;
import org.sopt.domain.Member;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FileMemberRepository implements MemberRepository {

    private final String filePath = "members.csv";
    // 인메모리 캐시로 사용할 HashMap
    private final Map<Long, Member> store = new HashMap<>();

    public FileMemberRepository() {
        loadDataFromFile(); // 생성자에서 파일 데이터를 메모리로 로드
    }

    // 프로그램 시작 시 파일 데이터를 메모리로 로드
    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Member member = parseMember(line.split(","));
                store.put(member.getId(), member);
            }
        } catch (FileNotFoundException e) {
            // 파일이 처음엔 없을 수 있으니 정상 처리
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 메모리의 현재 상태를 파일에 통째로 덮어쓰기
    private void flushToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) { // false: 덮어쓰기
            for (Member member : store.values()) {
                String memberCsv = String.format("%d,%s,%s,%s,%s\n",
                        member.getId(), member.getName(), member.getBirthdate(),
                        member.getEmail(), member.getGender());
                writer.write(memberCsv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Member save(Member member) {
        store.put(member.getId(), member);
        flushToFile();
        return member;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
        flushToFile();
    }

    // --- 메모리에서 바로 조회 ---

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

    private Member parseMember(String[] data) {
        Long id = Long.parseLong(data[0]);
        String name = data[1];
        LocalDate birthdate = LocalDate.parse(data[2]);
        String email = data[3];
        Gender gender = Gender.valueOf(data[4]);

        Member member = new Member(name, birthdate, email, gender);
        member.setId(id);
        return member;
    }
}