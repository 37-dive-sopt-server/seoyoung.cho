package org.sopt.domain.comment.dto;

import org.sopt.domain.comment.domain.Comment;

import java.util.List;

public record CommentListResponse(
        List<CommentResponse> comments,
        long totalCount
) {
    public static CommentListResponse from(List<Comment> comments) {
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from)
                .toList();

        return new CommentListResponse(
                commentResponses,
                commentResponses.size()
        );
    }
}
