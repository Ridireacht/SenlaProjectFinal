package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Comment;
import com.senla.project.entity.User;
import com.senla.project.mapper.CommentMapper;
import com.senla.project.mapper.CommentMapperImpl;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.CommentRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.CommentServiceImpl;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { CommentRepository.class, AdRepository.class, UserRepository.class, CommentServiceImpl.class, CommentMapperImpl.class })
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

  @MockBean
  private CommentRepository commentRepository;

  @MockBean
  private AdRepository adRepository;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private CommentService commentService;


  @Test
  public void testGetAdId() {
    long commentId = 1L;

    Comment comment = createComment(commentId, 2L, 3L);

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    long result = commentService.getAdId(commentId);

    assertEquals(comment.getAd().getId(), result);
  }

  @Test
  public void testGetCommentsOnAd() {
    long adId = 1L;

    List<Comment> comments = Arrays.asList(createComment(1L, 2L, adId), createComment(2L, 2L, adId));

    when(commentRepository.findAllByAdId(adId)).thenReturn(comments);

    List<CommentResponse> result = commentService.getCommentsOnAd(adId);

    assertEquals(comments.size(), result.size());
  }

  @Test
  public void testCreateCommentOnAd() {
    long senderId = 1L;
    long adId = 2L;

    CommentRequest commentRequest = createCommentRequest();
    User sender = createUser(senderId);
    Ad ad = createAd(adId);

    when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
    when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    CommentResponse result = commentService.createCommentOnAd(senderId, adId, commentRequest);

    assertNotNull(result);
    assertEquals(commentRequest.getContent(), result.getContent());
    assertEquals(sender.getId(), result.getSenderId());
  }

  @Test
  public void testUpdateComment() {
    long commentId = 1L;

    CommentRequest commentRequest = createCommentRequest();
    Comment existingComment = createComment(commentId, 2L, 3L);

    when(commentRepository.existsById(commentId)).thenReturn(true);
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
    when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    boolean result = commentService.updateComment(commentId, commentRequest);

    assertTrue(result);
  }

  @Test
  public void testDeleteComment() {
    long commentId = 1L;

    when(commentRepository.existsById(commentId)).thenReturn(true);

    boolean result = commentService.deleteComment(commentId);

    assertTrue(result);
  }

  @Test
  public void testDoesCommentExist() {
    long commentId = 1L;

    when(commentRepository.existsById(commentId)).thenReturn(true);

    boolean result = commentService.doesCommentExist(commentId);

    assertTrue(result);
  }

  @Test
  public void testDoesCommentBelongToUser() {
    long commentId = 1L;
    long senderId = 2L;

    Comment comment = createComment(commentId, senderId, 3L);
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    boolean result = commentService.doesCommentBelongToUser(commentId, senderId);

    assertTrue(result);
  }

  private Comment createComment(long commentId, long userId, long adId) {
    Comment comment = new Comment();
    comment.setId(commentId);
    comment.setPostedAt(LocalDateTime.now());
    comment.setContent("Test Comment");

    User sender = createUser(userId);
    comment.setSender(sender);

    Ad ad = createAd(adId);
    comment.setAd(ad);

    return comment;
  }

  private CommentRequest createCommentRequest() {
    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Test Comment");
    return commentRequest;
  }

  private User createUser(long userId) {
    User user = new User();
    user.setId(userId);
    return user;
  }

  private Ad createAd(long adId) {
    Ad ad = new Ad();
    ad.setId(adId);
    return ad;
  }
}
