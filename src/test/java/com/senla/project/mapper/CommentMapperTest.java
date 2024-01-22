package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.CommentRequest;
import com.senla.project.dto.CommentResponse;
import com.senla.project.entities.Comment;
import com.senla.project.entities.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentMapperTest {

  private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);


  @Test
  public void testMapToCommentResponse() {
    User user = new User();
    user.setId(2L);

    Comment comment = new Comment();
    comment.setId(1L);
    comment.setSender(user);
    comment.setContent("testContent");
    comment.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));

    CommentResponse expectedResponse = new CommentResponse();
    expectedResponse.setId(1L);
    expectedResponse.setSenderId(2L);
    expectedResponse.setContent("testContent");
    expectedResponse.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));

    CommentResponse actualResponse = mapper.mapToCommentResponse(comment);

    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getSenderId(), actualResponse.getSenderId());
    assertEquals(expectedResponse.getContent(), actualResponse.getContent());
    assertEquals(expectedResponse.getPostedAt(), actualResponse.getPostedAt());
  }

  @Test
  public void testMapToComment() {
    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("testContent");

    Comment expectedEntity = new Comment();
    expectedEntity.setContent("testContent");

    Comment actualEntity = mapper.mapToComment(commentRequest);

    assertEquals(expectedEntity.getContent(), actualEntity.getContent());
  }
}