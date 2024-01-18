package com.senla.project.services;

import com.senla.project.dto.CommentRequest;
import com.senla.project.dto.CommentResponse;
import java.util.List;

public interface CommentService {
  List<CommentResponse> getAllCommentsByAdId(Long id);

  CommentResponse createComment(Long adId, CommentRequest commentRequest);

  CommentResponse updateComment(Long commentId, CommentRequest commentRequest);

  boolean deleteComment(Long id);
}
