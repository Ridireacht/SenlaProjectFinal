package com.senla.project.services;

import com.senla.project.entities.Comment;
import java.util.List;

public interface CommentService {

  List<Comment> getAllComments();

  List<Comment> getCurrentAdComments();

  Comment createComment();

  Comment updateComment();

  boolean deleteComment();
}
