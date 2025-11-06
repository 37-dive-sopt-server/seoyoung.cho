package org.sopt.global.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.sopt.member.domain.Gender;
import org.sopt.global.exception.InvalidFormatException;

import java.io.IOException;
import java.util.Arrays;

public class GenderDeserializer extends JsonDeserializer<Gender> {
    @Override
    public Gender deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase();
        try {
            return Gender.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException("유효하지 않은 성별입니다. 허용된 값: " + Arrays.toString(Gender.values()));
        }
    }
}
