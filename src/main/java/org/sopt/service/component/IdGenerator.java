package org.sopt.service.component;

public interface IdGenerator {
    long generate();
    void initialize(long initialValue);
}
