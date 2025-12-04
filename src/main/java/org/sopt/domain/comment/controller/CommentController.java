package org.sopt.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.comment.domain.Comment;
import org.sopt.domain.comment.dto.CommentListResponse;
import org.sopt.domain.comment.dto.CommentRequest;
import org.sopt.domain.comment.dto.CommentResponse;
import org.sopt.domain.comment.service.CommentService;
import org.sopt.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comments", description = "댓글 API")
@RestController
@RequestMapping("/api/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long articleId,
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody CommentRequest request
    ) {
        Comment comment = commentService.createComment(articleId, memberId, request);
        CommentResponse response = CommentResponse.from(comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<CommentListResponse>> getComments(
            @PathVariable Long articleId
    ) {
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        CommentListResponse response = CommentListResponse.from(comments);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "댓글 수정", description = "본인이 작성한 댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody CommentRequest request
    ) {
        Comment comment = commentService.updateComment(articleId, commentId, memberId, request);
        CommentResponse response = CommentResponse.from(comment);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "댓글 삭제", description = "본인이 작성한 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Long memberId
    ) {
        commentService.deleteComment(articleId, commentId, memberId);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
