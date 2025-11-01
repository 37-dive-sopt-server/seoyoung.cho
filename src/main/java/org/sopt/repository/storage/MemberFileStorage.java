package org.sopt.repository.storage;

import org.sopt.domain.Gender;
import org.sopt.domain.Member;
import org.sopt.exception.DataStorageException;
import org.sopt.exception.InvalidCsvFormatException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class MemberFileStorage {
    private static final String FILE_PATH = "members.csv";
    private static final String DELIMITER = ",";
    private static final String CSV_FORMAT = "%d" + DELIMITER + "%s" + DELIMITER + "%s" + DELIMITER + "%s" + DELIMITER + "%s\n";
    private static final int CSV_COLUMN_COUNT = 5;

    public MemberFileStorage() {
        // 파일이 없으면 생성
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new DataStorageException("스토리지 파일을 생성할 수 없습니다.", e);
        }
    }

    public Map<Long, Member> loadFromFile() {
        Map<Long, Member> store = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                try {
                    Member member = parseMember(line.split(DELIMITER));
                    store.put(member.getId(), member);
                } catch (InvalidCsvFormatException e) {
                    System.err.println("CSV 파싱 오류 - " + lineNumber + "번째 줄: " + e.getMessage());
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            // 파일이 없는 것은 정상
        } catch (IOException e) {
            throw new DataStorageException("데이터 로드 중 오류가 발생했습니다.", e);
        }
        return store;
    }

    public void saveToFile(Map<Long, Member> store) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Member member : store.values()) {
                String memberCsv = String.format(CSV_FORMAT,
                        member.getId(), member.getName(), member.getBirthdate(),
                        member.getEmail(), member.getGender());
                writer.write(memberCsv);
            }
        } catch (IOException e) {
            throw new DataStorageException("데이터 저장 중 오류가 발생했습니다.", e);
        }
    }

    private Member parseMember(String[] data) {
        // CSV 형식 검증
        if (data.length != CSV_COLUMN_COUNT) {
            throw new InvalidCsvFormatException("CSV 열 개수가 올바르지 않습니다. (예상: " + CSV_COLUMN_COUNT + ", 실제: " + data.length + ")");
        }

        try {
            Long id = Long.parseLong(data[0]);
            String name = data[1];
            LocalDate birthdate = LocalDate.parse(data[2]);
            String email = data[3];
            Gender gender = Gender.valueOf(data[4]);
            return new Member(id, name, birthdate, email, gender);
        } catch (Exception e) {
            throw new InvalidCsvFormatException("CSV 데이터 파싱 중 오류: " + e.getMessage(), e);
        }
    }
}