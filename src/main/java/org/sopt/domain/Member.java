package org.sopt.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthdate;
    private String email;
    private Gender gender;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    public Member(String name, LocalDate birthdate, String email, Gender gender) {
        this(null, name, birthdate, email, gender);
    }

    public Member(Long id, String name, LocalDate birthdate, String email, Gender gender) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.email = email;
        this.gender = gender;
    }

    public int getAge() {
        return Period.between(this.birthdate, LocalDate.now()).getYears();
    }
}
