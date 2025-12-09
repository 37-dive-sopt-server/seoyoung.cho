package org.sopt.domain.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.comment.domain.Comment;
import org.sopt.domain.comment.dto.CommentRequest;
import org.sopt.domain.comment.exception.CommentForbiddenException;
import org.sopt.domain.comment.exception.CommentNotFoundException;
import org.sopt.domain.comment.repository.CommentRepository;
import org.sopt.domain.member.domain.Member;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.global.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Comment createComment(Long articleId, Long memberId, CommentRequest request) {
        log.info("💬 댓글 작성 시작 - articleId: {}, memberId: {}", articleId, memberId);

        Article article = findArticleById(articleId);

        Member member = findMemberById(memberId);

        Comment comment = Comment.create(article, member, request.content());
        Comment savedComment = commentRepository.save(comment);

        log.info("✅ 댓글 작성 완료 - commentId: {}", savedComment.getId());
        return savedComment;
    }

    @Override
    public List<Comment> getCommentsByArticleId(Long articleId) {
        log.info("📋 댓글 목록 조회 - articleId: {}", articleId);

        validateArticleExists(articleId);

        return commentRepository.findByArticleIdOrderByCreatedAtAsc(articleId);
    }

    @Override
    @Transactional
    public Comment updateComment(Long articleId, Long commentId, Long memberId, CommentRequest request) {
        log.info("✏️ 댓글 수정 시작 - commentId: {}, memberId: {}", commentId, memberId);

        Comment comment = findCommentByIdAndValidate(commentId, articleId, memberId);

        comment.updateContent(request.content());

        log.info("✅ 댓글 수정 완료 - commentId: {}", commentId);
        return comment;
    }

    @Override
    @Transactional
    public void deleteComment(Long articleId, Long commentId, Long memberId) {
        log.info("🗑️ 댓글 삭제 시작 - commentId: {}, memberId: {}", commentId, memberId);

        Comment comment = findCommentByIdAndValidate(commentId, articleId, memberId);

        commentRepository.delete(comment);

        log.info("✅ 댓글 삭제 완료 - commentId: {}", commentId);
    }

    /* 게시글 조회 */
    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(EntityNotFoundException::new);
    }

    /* 회원 조회 */
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);
    }

    /* 게시글 존재 확인 */
    private void validateArticleExists(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new EntityNotFoundException();
        }
    }

    /* 댓글 조회 및 권한 검증 */
    private Comment findCommentByIdAndValidate(Long commentId, Long articleId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getArticle().getId().equals(articleId)) {
            throw new CommentNotFoundException();
        }

        if (!comment.isWrittenBy(memberId)) {
            throw new CommentForbiddenException();
        }

        return comment;
    }

    @Override
    public Page<Comment> getCommentsByArticleId(Long articleId, Pageable pageable) {
        log.info("📋 댓글 목록 조회 (페이지네이션) - articleId: {}, page: {}", articleId, pageable.getPageNumber());

        validateArticleExists(articleId);

        return commentRepository.findByArticleIdOrderByCreatedAtAsc(articleId, pageable);
    }
}
