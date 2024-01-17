package com.senla.project.services.impl;

import com.senla.project.repositories.CommentRepository;
import com.senla.project.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
}
