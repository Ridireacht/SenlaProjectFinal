package com.senla.project.services;

import com.senla.project.dto.CommentRequest;
import com.senla.project.dto.CommentResponse;
import java.util.List;

public interface CommentService {
  List<CommentResponse> getAllCommentsByAdId(Long adId);

  CommentResponse createComment(Long userId, Long adId, CommentRequest commentRequest);

  CommentResponse updateComment(Long commentId, CommentRequest commentRequest);

  boolean deleteComment(Long commentId);

  boolean doesCommentExist(Long commentId);
}
