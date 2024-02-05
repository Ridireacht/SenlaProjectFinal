package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Comment;
import com.senla.project.entity.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = { CommentMapperImpl.class })
public class CommentMapperTest {

  @Autowired
  private CommentMapper commentMapper;


  @Test
  public void testMapToCommentResponse() {
    Comment comment = createComment();
    CommentResponse commentResponse = commentMapper.mapToCommentResponse(comment);

    assertEquals(comment.getId(), commentResponse.getId());
    assertEquals(comment.getSender().getId(), commentResponse.getSenderId());
    assertEquals(comment.getContent(), commentResponse.getContent());
    assertEquals(comment.getPostedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), commentResponse.getPostedAt());
  }

  @Test
  public void testMapToComment() {
    CommentRequest commentRequest = createCommentRequest();
    Comment comment = commentMapper.mapToComment(commentRequest);

    assertEquals(commentRequest.getContent(), comment.getContent());
  }

  private Comment createComment() {
    Comment comment = new Comment();
    comment.setId(1L);
    comment.setPostedAt(LocalDateTime.now());
    comment.setContent("Test Comment");

    User sender = new User();
    sender.setId(2L);
    comment.setSender(sender);

    Ad ad = new Ad();
    ad.setId(3L);
    comment.setAd(ad);

    return comment;
  }

  private CommentRequest createCommentRequest() {
    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Test Comment");
    return commentRequest;
  }
}
