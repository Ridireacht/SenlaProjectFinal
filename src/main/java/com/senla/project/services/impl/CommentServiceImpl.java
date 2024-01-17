package com.senla.project.services.impl;

import com.senla.project.dto.CommentRequest;
import com.senla.project.dto.CommentResponse;
import com.senla.project.repositories.CommentRepository;
import com.senla.project.services.CommentService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;


  @Override
  public List<CommentResponse> getAllComments() {
    return null;
  }

  @Override
  public List<CommentResponse> getCommentsByAdId(Long id) {
    return null;
  }

  @Override
  public CommentResponse createComment(CommentRequest commentRequest) {
    return null;
  }

  @Override
  public CommentResponse updateComment(CommentRequest commentRequest) {
    return null;
  }

  @Override
  public boolean deleteComment(Long id) {
    return false;
  }
}
