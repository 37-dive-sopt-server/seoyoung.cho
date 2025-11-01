package org.sopt.domain;

import java.time.LocalDate;
import java.time.Period;

public class Member {

    private Long id;
    private String name;
    private LocalDate birthdate;
    private String email;
    private Gender gender;

    public Member() {
    }

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return Period.between(this.birthdate, LocalDate.now()).getYears();
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    public Gender getGender() {
        return gender;
    }
}
