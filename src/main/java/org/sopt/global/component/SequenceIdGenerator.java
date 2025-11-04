package org.sopt.global.component;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class SequenceIdGenerator implements IdGenerator{
    private final AtomicLong sequence = new AtomicLong(0L); // 동시성 보장

    @Override
    public long generate() {
        return sequence.incrementAndGet();
    }

    @Override
    public void initialize(long initialValue) {
        sequence.set(initialValue);
    }
}
