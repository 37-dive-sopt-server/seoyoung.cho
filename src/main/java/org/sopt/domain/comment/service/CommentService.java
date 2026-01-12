package org.sopt.domain.comment.service;

import org.sopt.domain.comment.domain.Comment;
import org.sopt.domain.comment.dto.CommentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    Comment createComment(Long articleId, Long memberId, CommentRequest request);

    List<Comment> getCommentsByArticleId(Long articleId);

    Comment updateComment(Long articleId, Long commentId, Long memberId, CommentRequest request);

    void deleteComment(Long articleId, Long commentId, Long memberId);

    // 페이지네이션 지원
    Page<Comment> getCommentsByArticleId(Long articleId, Pageable pageable);
}