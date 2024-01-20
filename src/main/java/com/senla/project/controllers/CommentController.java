package com.senla.project.controllers;

import com.senla.project.dto.CommentRequest;
import com.senla.project.dto.CommentResponse;
import com.senla.project.services.CommentService;
import com.senla.project.services.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
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


  @GetMapping
  public List<CommentResponse> getAllComments(@PathVariable("adId") Long adId) {
    return commentService.getAllCommentsByAdId(adId);
  }

  @PostMapping
  public CommentResponse createComment(@PathVariable("adId") Long adId, CommentRequest commentRequest) {
    return commentService.createComment(getCurrentUserId(), adId, commentRequest);
  }

  @PutMapping("/{commentId}")
  public CommentResponse updateComment(@PathVariable("commentId") Long commentId, CommentRequest commentRequest) {
    return commentService.updateComment(commentId, commentRequest);
  }

  @DeleteMapping("/{commentId}")
  public boolean deleteComment(@PathVariable("commentId") Long commentId) {
    return commentService.deleteComment(commentId);
  }


  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
