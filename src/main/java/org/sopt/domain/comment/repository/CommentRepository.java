package org.sopt.domain.comment.repository;

import org.sopt.domain.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 모든 댓글 조회 (생성 오름차순)
    @EntityGraph(attributePaths = {"member", "article"})
    List<Comment> findByArticleIdOrderByCreatedAtAsc(Long articleId);

    // 특정 게시글의 댓글 개수 조회 (카운트 쿼리만 실행)
    long countByArticleId(Long articleId);

    // 페이지네이션 지원 - N+1 방지
    @EntityGraph(attributePaths = {"member", "article"})
    Page<Comment> findByArticleIdOrderByCreatedAtAsc(Long articleId, Pageable pageable);
}
