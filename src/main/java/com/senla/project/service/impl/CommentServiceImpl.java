package com.senla.project.service.impl;

import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Comment;
import com.senla.project.entity.User;
import com.senla.project.mapper.CommentMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.CommentRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.CommentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final AdRepository adRepository;
  private final UserRepository userRepository;

  private final CommentMapper commentMapper;


  @Override
  public long getAdId(long commentId) {
    Comment comment = commentRepository.findById(commentId).get();
    return comment.getAd().getId();
  }

  @Override
  public List<CommentResponse> getCommentsOnAd(long adId) {
    List<Comment> comments = commentRepository.findAllByAdId(adId);
    return comments.stream()
        .map(commentMapper::mapToCommentResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public CommentResponse createCommentOnAd(long userId, long adId, CommentRequest commentRequest) {
    User sender = userRepository.findById(userId).get();
    Ad ad = adRepository.findById(adId).get();

    Comment comment = commentMapper.mapToComment(commentRequest);
    comment.setSender(sender);
    comment.setAd(ad);
    comment.setContent(commentRequest.getContent());
    comment.setPostedAt(LocalDateTime.now());

    Comment savedComment = commentRepository.save(comment);
    return commentMapper.mapToCommentResponse(savedComment);
  }

  @Transactional
  @Override
  public boolean updateComment(long commentId, CommentRequest commentRequest) {
    if (commentRepository.existsById(commentId)) {
      Comment existingComment = commentRepository.findById(commentId).get();

      existingComment.setContent(commentRequest.getContent());
      existingComment.setPostedAt(LocalDateTime.now());

      commentRepository.save(existingComment);
      return true;
    }

    return false;
  }

  @Transactional
  @Override
  public boolean deleteComment(long commentId) {
    if (commentRepository.existsById(commentId)) {
      commentRepository.deleteById(commentId);
      return true;
    }

    return false;
  }

  @Override
  public boolean doesCommentExist(long commentId) {
    return commentRepository.existsById(commentId);
  }

  @Override
  public boolean doesCommentBelongToUser(long commentId, long userId) {
    Comment comment = commentRepository.findById(commentId).get();
    return comment.getSender().getId() == userId;
  }

}
