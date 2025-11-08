package org.sopt.article.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.sopt.article.domain.Tag;
import org.sopt.global.deserializer.TagDeserializer;

public record ArticleCreateRequest(

        @NotNull(message = "userId는 필수 입력 항목입니다.")
        Long userId,

        @NotBlank(message = "게시글 제목은 필수 입력 항목입니다.")
        String title,

        @NotBlank(message = "게시글 내용은 필수 입력 항목입니다.")
        String content,

        @NotNull(message = "태그는 필수 입력 항목입니다.")
        @JsonDeserialize(using = TagDeserializer.class)
        Tag tag
) {
}
