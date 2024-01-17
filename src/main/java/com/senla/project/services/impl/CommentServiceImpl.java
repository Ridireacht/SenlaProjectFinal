package com.senla.project.services.impl;

import com.senla.project.entities.Comment;
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
  public List<Comment> getAllComments() {
    return null;
  }

  @Override
  public List<Comment> getCurrentAdComments() {
    return null;
  }

  @Override
  public Comment createComment() {
    return null;
  }

  @Override
  public Comment updateComment() {
    return null;
  }

  @Override
  public boolean deleteComment() {
    return false;
  }
}
