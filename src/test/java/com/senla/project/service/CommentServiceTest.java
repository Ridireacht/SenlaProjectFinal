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
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.CommentRepository;
import com.senla.project.repository.UserRepository;
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

@SpringBootTest
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

    Comment comment = createComment();

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    long result = commentService.getAdId(commentId);

    assertEquals(comment.getAd().getId(), result);
  }

  @Test
  public void testGetCommentsOnAd() {
    long adId = 1L;

    List<Comment> comments = Arrays.asList(createComment(), createComment());

    when(commentRepository.findAllByAdId(adId)).thenReturn(comments);

    List<CommentResponse> result = commentService.getCommentsOnAd(adId);

    assertEquals(comments.size(), result.size());
  }

  @Test
  public void testCreateCommentOnAd() {
    long userId = 1L;
    long adId = 2L;

    CommentRequest commentRequest = createCommentRequest();
    User sender = createUser();
    Ad ad = createAd();

    when(userRepository.findById(userId)).thenReturn(Optional.of(sender));
    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
    when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    CommentResponse result = commentService.createCommentOnAd(userId, adId, commentRequest);

    assertNotNull(result);
    assertEquals(commentRequest.getContent(), result.getContent());
    assertEquals(sender.getId(), result.getSenderId());
  }

  @Test
  public void testUpdateComment() {
    long commentId = 1L;

    CommentRequest commentRequest = createCommentRequest();
    Comment existingComment = createComment();

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
    long userId = 2L;

    Comment comment = createComment();
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    boolean result = commentService.doesCommentBelongToUser(commentId, userId);

    assertTrue(result);
  }

  private Comment createComment() {
    Comment comment = new Comment();
    comment.setId(1L);
    comment.setPostedAt(LocalDateTime.now());
    comment.setContent("Test Comment");

    User sender = createUser();
    comment.setSender(sender);

    Ad ad = createAd();
    comment.setAd(ad);

    return comment;
  }

  private CommentRequest createCommentRequest() {
    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Test Comment");
    return commentRequest;
  }

  private User createUser() {
    User user = new User();
    user.setId(2L);
    return user;
  }

  private Ad createAd() {
    Ad ad = new Ad();
    ad.setId(3L);
    return ad;
  }
}
