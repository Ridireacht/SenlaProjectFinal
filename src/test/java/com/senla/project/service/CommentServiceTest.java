package com.senla.project.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Comment;
import com.senla.project.entity.User;
import com.senla.project.mapper.CommentMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.CommentRepository;
import com.senla.project.repository.UserRepository;
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
  CommentRepository commentRepository;

  @MockBean
  AdRepository adRepository;

  @MockBean
  UserRepository userRepository;

  @MockBean
  CommentMapper commentMapper;

  @Autowired
  CommentService commentService;


  @Test
  void testGetAllCommentsByAdId() {
    Long adId = 1L;

    Comment comment1 = new Comment();
    Comment comment2 = new Comment();

    comment1.setContent("testContent1");
    comment2.setContent("testContent2");

    CommentResponse expectedCommentResponse1 = new CommentResponse();
    CommentResponse expectedCommentResponse2 = new CommentResponse();

    expectedCommentResponse1.setContent(comment1.getContent());
    expectedCommentResponse2.setContent(comment2.getContent());

    when(commentRepository.findAllByAd_Id(adId)).thenReturn(Arrays.asList(comment1, comment2));
    when(commentMapper.mapToCommentResponse(comment1)).thenReturn(expectedCommentResponse1);
    when(commentMapper.mapToCommentResponse(comment2)).thenReturn(expectedCommentResponse2);

    List<CommentResponse> actualCommentResponses = commentService.getAllCommentsByAdId(adId);

    assertEquals(2, actualCommentResponses.size());
    assertThat(actualCommentResponses, contains(
        hasProperty("content", is("testContent1")),
        hasProperty("content", is("testContent2"))
    ));
  }

  @Test
  void testCreateComment() {
    Long userId = 1L;
    Long adId = 2L;

    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("testContent");

    User user = new User();
    user.setId(userId);

    Ad ad = new Ad();
    ad.setId(adId);

    Comment expectedComment = new Comment();
    expectedComment.setSender(user);
    expectedComment.setContent(commentRequest.getContent());

    CommentResponse expectedCommentResponse = new CommentResponse();
    expectedCommentResponse.setSenderId(userId);
    expectedCommentResponse.setContent(commentRequest.getContent());

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
    when(commentRepository.save(any(Comment.class))).thenReturn(expectedComment);
    when(commentMapper.mapToCommentResponse(expectedComment)).thenReturn(expectedCommentResponse);

    CommentResponse actualCommentResponse = commentService.createComment(userId, adId, commentRequest);

    assertEquals(expectedCommentResponse.getSenderId(), actualCommentResponse.getSenderId());
    assertEquals(expectedCommentResponse.getContent(), actualCommentResponse.getContent());
  }

  @Test
  void testUpdateComment() {
    Long commentId = 1L;

    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("testContent");

    Comment expectedExistingComment = new Comment();
    expectedExistingComment.setContent("oldTestContent");

    Comment expectedUpdatedComment = new Comment();
    expectedExistingComment.setContent(commentRequest.getContent());

    CommentResponse expectedCommentResponse = new CommentResponse();
    expectedCommentResponse.setContent(expectedCommentResponse.getContent());

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(expectedExistingComment));
    when(commentRepository.save(any(Comment.class))).thenReturn(expectedUpdatedComment);
    when(commentMapper.mapToCommentResponse(expectedUpdatedComment)).thenReturn(expectedCommentResponse);

    CommentResponse actualCommentResponse = commentService.updateComment(commentId, commentRequest);

    assertEquals(expectedCommentResponse.getContent(), actualCommentResponse.getContent());
  }

  @Test
  void testDeleteComment() {
    Long commentId = 1L;

    when(commentRepository.existsById(commentId)).thenReturn(true);

    boolean expectedResult = commentService.deleteComment(commentId);

    assertTrue(expectedResult);
  }

  @Test
  void testDoesCommentExist() {
    Long commentId = 1L;

    when(commentRepository.existsById(commentId)).thenReturn(true);

    boolean expectedResult = commentService.doesCommentExist(commentId);

    assertTrue(expectedResult);
  }

  @Test
  void testDoesCommentBelongToUser() {
    Long commentId = 1L;
    Long currentUserId = 2L;

    Comment comment = new Comment();

    User user = new User();
    user.setId(currentUserId);

    comment.setSender(user);

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    boolean expectedResult = commentService.doesCommentBelongToUser(commentId, currentUserId);

    assertTrue(expectedResult);
  }
}
