package com.senla.project.services;

import com.senla.project.dto.CommentRequest;
import com.senla.project.dto.CommentResponse;
import java.util.List;

public interface CommentService {

  List<CommentResponse> getAllComments();

  List<CommentResponse> getCommentsByAdId(Long id);

  CommentResponse createComment(CommentRequest commentRequest);

  CommentResponse updateComment(CommentRequest commentRequest);

  boolean deleteComment(Long id);
}
