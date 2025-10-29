package org.sopt.component;

public interface IdGenerator {
    long generate();
    void initialize(long initialValue);
}
