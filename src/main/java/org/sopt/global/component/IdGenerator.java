package org.sopt.global.component;

public interface IdGenerator {
    long generate();
    void initialize(long initialValue);
}
