package org.sopt.view;

import org.sopt.controller.MemberController;
import org.sopt.domain.Gender;
import org.sopt.domain.Member;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MemberView {
    private final Scanner scanner = new Scanner(System.in);
    private final MemberController memberController;

    public MemberView(MemberController memberController) {
        this.memberController = memberController;
    }

    // 프로그램 실행 메소드
    public void run() {
        while (true) {
            System.out.println("\n✨ --- DIVE SOPT 회원 관리 서비스 --- ✨");
            System.out.println("---------------------------------");
            System.out.println("1️⃣. 회원 등록 ➕");
            System.out.println("2️⃣. ID로 회원 조회 🔍");
            System.out.println("3️⃣. 전체 회원 조회 📋");
            System.out.println("4️⃣. ID로 회원 삭제 🗑️");
            System.out.println("5️⃣. 종료 🚪");
            System.out.println("---------------------------------");
            System.out.print("메뉴를 선택하세요: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> registerMember();
                case "2" -> findMemberById();
                case "3" -> findAllMembers();
                case "4" -> deleteMemberById();
                case "5" -> {
                    System.out.println("👋 서비스를 종료합니다. 안녕히 계세요!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("🚫 잘못된 메뉴 선택입니다. 다시 시도해주세요.");
            }
        }
    }

    private void registerMember() {
        try {
            System.out.print("등록할 회원 이름을 입력하세요: ");
            String name = scanner.nextLine();

            System.out.print("생년월일(YYYY-MM-DD)을 입력하세요: ");
            String birthdateStr = scanner.nextLine();

            System.out.print("이메일을 입력하세요: ");
            String email = scanner.nextLine();

            System.out.print("성별(MALE/FEMALE/OTHER)을 입력하세요: ");
            String genderStr = scanner.nextLine();

            if (name.trim().isEmpty() || email.trim().isEmpty() ||
                    birthdateStr.trim().isEmpty() || genderStr.trim().isEmpty()) {
                System.out.println("⚠️ 모든 항목을 빠짐없이 입력해주세요.");
                return;
            }

            LocalDate birthdate = LocalDate.parse(birthdateStr);
            Gender gender = Gender.valueOf(genderStr.trim().toUpperCase());

            Member newMember = new Member(name, birthdate, email, gender);
            Long createdId = memberController.createMember(newMember);

            System.out.println("✅ 회원 등록 완료 (ID: " + createdId + ")");

        } catch (DateTimeParseException e) {
            System.out.println("❌ 날짜 형식이 올바르지 않습니다. (YYYY-MM-DD 형식으로 입력해주세요)");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 성별을 잘못 입력했습니다. (MALE 또는 FEMALE 또는 OTHER로 입력해주세요)");
        } catch (IllegalStateException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ 알 수 없는 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void findMemberById() {
        System.out.print("조회할 회원 ID를 입력하세요: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Optional<Member> foundMember = memberController.findMemberById(id);
            if (foundMember.isPresent()) {
                Member member = foundMember.get();
                System.out.println("--- ✅ 조회된 회원 정보 ---");
                System.out.println("ID: " + member.getId());
                System.out.println("이름: " + member.getName());
                System.out.printf("나이: 만 %d세\n", member.getAge()); // 나이 정보 추가
                System.out.println("생년월일: " + member.getBirthdate());
                System.out.println("이메일: " + member.getEmail());
                System.out.println("성별: " + member.getGender());
                System.out.println("--------------------------");
            } else {
                System.out.println("⚠️ 해당 ID의 회원을 찾을 수 없습니다.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ 유효하지 않은 ID 형식입니다. 숫자를 입력해주세요.");
        }
    }

    private void findAllMembers() {
        List<Member> allMembers = memberController.getAllMembers();
        if (allMembers.isEmpty()) {
            System.out.println("ℹ️ 등록된 회원이 없습니다.");
        } else {
            System.out.println("--- 📋 전체 회원 목록 📋 ---");
            for (Member member : allMembers) {
                System.out.printf("👤 ID: %d, 이름: %s, 생년월일: %s, 나이: %d, 이메일: %s\n",
                        member.getId(), member.getName(), member.getBirthdate() , member.getAge(), member.getEmail());
            }
            System.out.println("--------------------------");
        }
    }

    private void deleteMemberById() {
        System.out.print("삭제할 회원 ID를 입력하세요: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            boolean isDeleted = memberController.deleteMember(id);
            if (isDeleted) {
                System.out.println("✅ 회원 삭제가 완료되었습니다.");
            } else {
                System.out.println("⚠️ 해당 ID의 회원을 찾을 수 없습니다.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ 유효하지 않은 ID 형식입니다. 숫자를 입력해주세요.");
        }
    }
}