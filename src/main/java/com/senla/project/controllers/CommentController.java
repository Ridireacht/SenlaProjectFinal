package com.senla.project.controllers;

import com.senla.project.dto.CommentRequest;
import com.senla.project.dto.CommentResponse;
import com.senla.project.services.AdService;
import com.senla.project.services.CommentService;
import com.senla.project.services.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ads/{adId}/comments")
@AllArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final UserService userService;
  private final AdService adService;


  @GetMapping
  public List<CommentResponse> getAllComments(@PathVariable("adId") Long adId) {
    return commentService.getAllCommentsByAdId(adId);
  }

  @PostMapping
  public ResponseEntity<CommentResponse> createComment(@PathVariable("adId") Long adId, CommentRequest commentRequest) {
    if (!adService.doesAdExist(adId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Здесь, в отличие от прочих использований этой конструкции, не используется отрицание -
    // создатель объявления НЕ должен иметь возможности оставить комментарий у себя же.
    if (adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(commentService.createComment(getCurrentUserId(), adId, commentRequest));
  }

  @PutMapping("/{commentId}")
  public ResponseEntity<CommentResponse> updateComment(@PathVariable("commentId") Long commentId, CommentRequest commentRequest) {
    if (!commentService.doesCommentExist(commentId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!adService.doesCommentBelongToUser(commentId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(commentService.updateComment(commentId, commentRequest));
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<Boolean> deleteComment(@PathVariable("commentId") Long commentId) {
    if (!commentService.doesCommentExist(commentId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!adService.doesCommentBelongToUser(commentId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(commentService.deleteComment(commentId));
  }


  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
