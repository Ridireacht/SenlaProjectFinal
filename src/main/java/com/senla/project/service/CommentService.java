package com.senla.project.service;

import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import java.util.List;

public interface CommentService {
  List<CommentResponse> getAllCommentsByAdId(Long adId);

  CommentResponse createComment(Long userId, Long adId, CommentRequest commentRequest);

  CommentResponse updateComment(Long commentId, CommentRequest commentRequest);

  boolean deleteComment(Long commentId);

  boolean doesCommentExist(Long commentId);

  boolean doesCommentBelongToUser(Long commentId, Long currentUserId);
}
