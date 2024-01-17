package com.senla.project.services;

import com.senla.project.dto.CommentRequest;
import com.senla.project.dto.CommentResponse;

public interface CommentService {

  CommentResponse createComment(CommentRequest commentRequest);

  CommentResponse updateComment(CommentRequest commentRequest);

  boolean deleteComment(Long id);
}
