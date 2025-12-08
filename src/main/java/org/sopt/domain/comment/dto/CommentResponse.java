package org.sopt.domain.comment.dto;

import org.sopt.domain.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String writerName,
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getMember().getName(),
                comment.getCreatedAt()
        );
    }
}
