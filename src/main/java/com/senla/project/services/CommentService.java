package com.senla.project.services;

import com.senla.project.entities.Comment;

public interface CommentService {
  Comment createComment();

  Comment updateComment();

  boolean deleteComment();
}
