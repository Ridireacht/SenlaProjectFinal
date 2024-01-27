package com.senla.project.controller;

import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AdService;
import com.senla.project.service.CommentService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Комментарий", description = "API для управления комментариями")
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
  public CommentResponse createComment(@PathVariable("adId") Long adId, @Valid @RequestBody CommentRequest commentRequest) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    // Здесь, в отличие от прочих использований этой конструкции, не используется отрицание -
    // создатель объявления НЕ должен иметь возможности оставить комментарий у себя же.
    if (adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      throw new ForbiddenException("You can't leave a comment on your own ad");
    }

    if (adService.isAdClosed(adId)) {
      throw new ForbiddenException("You can't leave a comment on a closed ad");
    }

    return commentService.createComment(getCurrentUserId(), adId, commentRequest);
  }

  @PutMapping("/{commentId}")
  public CommentResponse updateComment(@PathVariable("commentId") Long commentId, @Valid @RequestBody CommentRequest commentRequest) {
    if (!commentService.doesCommentExist(commentId)) {
      throw new NotFoundException("Comment", commentId);
    }

    if (!commentService.doesCommentBelongToUser(commentId, getCurrentUserId())) {
      throw new ForbiddenException("You can't update someone else's comment");
    }

    if (adService.isAdClosed(commentService.getAdId(commentId))) {
      throw new ForbiddenException("You can't update a comment on a closed ad");
    }

    return commentService.updateComment(commentId, commentRequest);
  }

  @DeleteMapping("/{commentId}")
  public Boolean deleteComment(@PathVariable("commentId") Long commentId) {
    if (!commentService.doesCommentExist(commentId)) {
      throw new NotFoundException("Comment", commentId);
    }

    if (!commentService.doesCommentBelongToUser(commentId, getCurrentUserId())) {
      throw new ForbiddenException("You can't delete someone else's comment");
    }

    if (adService.isAdClosed(commentService.getAdId(commentId))) {
      throw new ForbiddenException("You can't delete a comment on a closed ad");
    }

    return commentService.deleteComment(commentId);
  }


  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
