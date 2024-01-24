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

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final AdRepository adRepository;
  private final UserRepository userRepository;

  private final CommentMapper commentMapper;


  @Override
  public List<CommentResponse> getAllCommentsByAdId(Long adId) {
    List<Comment> comments = commentRepository.findAllByAd_Id(adId);

    return comments.stream()
        .map(commentMapper::mapToCommentResponse)
        .collect(Collectors.toList());
  }

  @Override
  public CommentResponse createComment(Long userId, Long adId, CommentRequest commentRequest) {
    User sender = userRepository.findById(userId).get();
    Ad ad = adRepository.findById(adId).get();

    Comment comment = new Comment();
    comment.setSender(sender);
    comment.setAd(ad);
    comment.setContent(commentRequest.getContent());
    comment.setPostedAt(LocalDateTime.now());

    Comment savedComment = commentRepository.save(comment);
    return commentMapper.mapToCommentResponse(savedComment);
  }

  @Override
  public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
    Comment existingComment = commentRepository.findById(commentId).get();

    existingComment.setContent(commentRequest.getContent());
    existingComment.setPostedAt(LocalDateTime.now());

    Comment updatedComment = commentRepository.save(existingComment);
    return commentMapper.mapToCommentResponse(updatedComment);
  }

  @Override
  public boolean deleteComment(Long commentId) {
    if (commentRepository.existsById(commentId)) {
      commentRepository.deleteById(commentId);
      return true;
    }

    return false;
  }

  @Override
  public boolean doesCommentExist(Long commentId) {
    return commentRepository.existsById(commentId);
  }

  @Override
  public boolean doesCommentBelongToUser(Long commentId, Long currentUserId) {
    Comment comment = commentRepository.findById(commentId).get();

    return comment.getSender().getId().equals(currentUserId);
  }
}
