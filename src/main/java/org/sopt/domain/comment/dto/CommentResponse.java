package org.sopt.domain.comment.dto;

import org.sopt.domain.comment.domain.Comment;
import org.sopt.domain.member.domain.Member;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        CommentWriterInfo writer,
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                CommentWriterInfo.from(comment.getMember()),
                comment.getCreatedAt()
        );
    }

    public record CommentWriterInfo(
            Long userId,
            String name
    ) {
        public static CommentWriterInfo from(Member member) {
            return new CommentWriterInfo(
                    member.getId(),
                    member.getName()
            );
        }
    }
}
