package org.sopt.domain.comment.repository;

import org.sopt.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 모든 댓글 조회 (생성 오름차순)
    List<Comment> findByArticleIdOrderByCreatedAtAsc(Long articleId);

}
