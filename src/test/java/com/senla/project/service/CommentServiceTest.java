package com.senla.project.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

  @Autowired
  CommentService commentService;


  @Test
  void testGetAllCommentsByAdId() {
    Long adId = 1L;

    Comment comment1 = new Comment();
    Comment comment2 = new Comment();

    comment1.setContent("testContent1");
    comment2.setContent("testContent2");

    when(commentRepository.findAllByAd_Id(adId)).thenReturn(Arrays.asList(comment1, comment2));

    List<CommentResponse> result = commentService.getAllCommentsByAdId(adId);

    assertEquals(2, result.size());
    assertThat(result, contains(
        hasProperty("content", is("testContent1")),
        hasProperty("content", is("testContent2"))
    ));
  }
}
