package com.senla.project.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.project.config.SecurityConfig;
import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import com.senla.project.exception.GlobalExceptionHandler;
import com.senla.project.service.AdService;
import com.senla.project.service.AuthService;
import com.senla.project.service.CommentService;
import com.senla.project.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = CommentController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    }
)
@ContextConfiguration(classes = { CommentController.class, GlobalExceptionHandler.class })
public class CommentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CommentService commentService;

  @MockBean
  private UserService userService;

  @MockBean
  private AdService adService;

  @MockBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  public void testGetCommentsOnAd_Success() throws Exception {
    Long adId = 1L;

    List<CommentResponse> expectedComments = new ArrayList<>();

    CommentResponse comment1 = new CommentResponse();
    comment1.setId(1L);
    comment1.setSenderId(1L);
    comment1.setContent("Test comment 1");
    comment1.setPostedAt("2024-02-07T10:15:30");

    CommentResponse comment2 = new CommentResponse();
    comment2.setId(2L);
    comment2.setSenderId(2L);
    comment2.setContent("Test comment 2");
    comment2.setPostedAt("2024-02-07T10:20:30");

    expectedComments.add(comment1);
    expectedComments.add(comment2);

    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.isAdClosed(adId)).thenReturn(false);
    when(userService.isUserBuyerOrSellerOfAd(anyLong(), eq(adId))).thenReturn(true);
    when(commentService.getCommentsOnAd(adId)).thenReturn(expectedComments);

    mockMvc.perform(get("/ads/{adId}/comments", adId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].senderId", is(1)))
        .andExpect(jsonPath("$[0].content", is("Test comment 1")))
        .andExpect(jsonPath("$[0].postedAt", is("2024-02-07T10:15:30")))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].senderId", is(2)))
        .andExpect(jsonPath("$[1].content", is("Test comment 2")))
        .andExpect(jsonPath("$[1].postedAt", is("2024-02-07T10:20:30")));
  }

  @Test
  public void testGetCommentsOnAd_AdNotFound() throws Exception {
    Long adId = 1L;

    when(adService.doesAdExist(adId)).thenReturn(false);

    mockMvc.perform(get("/ads/{adId}/comments", adId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetCommentsOnAd_AdClosedAndNotBuyerOrSeller() throws Exception {
    Long adId = 1L;

    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.isAdClosed(adId)).thenReturn(true);
    when(userService.isUserBuyerOrSellerOfAd(anyLong(), eq(adId))).thenReturn(false);

    mockMvc.perform(get("/ads/{adId}/comments", adId))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testCreateCommentOnAd_Success() throws Exception {
    Long adId = 1L;
    Long userId = 1L;

    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Test comment content");

    CommentResponse expectedComment = new CommentResponse();
    expectedComment.setId(1L);
    expectedComment.setSenderId(userId);
    expectedComment.setContent("Test comment content");
    expectedComment.setPostedAt("2024-02-07T10:30:45");

    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.doesAdBelongToUser(adId, userId)).thenReturn(false);
    when(adService.isAdClosed(adId)).thenReturn(false);
    when(authService.getCurrentUserId()).thenReturn(userId);
    when(commentService.createCommentOnAd(eq(userId), eq(adId), any(CommentRequest.class))).thenReturn(expectedComment);

    mockMvc.perform(post("/ads/{adId}/comments", adId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.senderId", is(1)))
        .andExpect(jsonPath("$.content", is("Test comment content")))
        .andExpect(jsonPath("$.postedAt", is("2024-02-07T10:30:45")));
  }

  @Test
  public void testCreateCommentOnAd_AdNotFound() throws Exception {
    Long adId = 1L;

    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Test comment content");

    when(adService.doesAdExist(adId)).thenReturn(false);

    mockMvc.perform(post("/ads/{adId}/comments", adId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testCreateCommentOnAd_AdBelongsToUser() throws Exception {
    Long adId = 1L;
    Long userId = 1L;

    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Test comment content");

    when(adService.doesAdExist(adId)).thenReturn(true);
    when(authService.getCurrentUserId()).thenReturn(userId);
    when(adService.doesAdBelongToUser(adId, userId)).thenReturn(true);

    mockMvc.perform(post("/ads/{adId}/comments", adId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testCreateCommentOnAd_AdClosed() throws Exception {
    Long adId = 1L;
    Long userId = 1L;

    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Test comment content");

    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.doesAdBelongToUser(adId, userId)).thenReturn(false);
    when(adService.isAdClosed(adId)).thenReturn(true);

    mockMvc.perform(post("/ads/{adId}/comments", adId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testUpdateComment_Success() throws Exception {
    Long commentId = 1L;
    Long userId = 1L;

    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Updated comment content");

    when(commentService.doesCommentExist(commentId)).thenReturn(true);
    when(commentService.doesCommentBelongToUser(commentId, userId)).thenReturn(true);
    when(adService.isAdClosed(anyLong())).thenReturn(false);
    when(authService.getCurrentUserId()).thenReturn(userId);
    when(commentService.updateComment(eq(commentId), any(CommentRequest.class))).thenReturn(true);

    mockMvc.perform(put("/ads/{adId}/comments/{commentId}", 1L, commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  public void testUpdateComment_CommentNotFound() throws Exception {
    Long commentId = 1L;
    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Updated comment content");

    when(commentService.doesCommentExist(commentId)).thenReturn(false);

    mockMvc.perform(put("/ads/{adId}/comments/{commentId}", 1L, commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateComment_CommentDoesNotBelongToUser() throws Exception {
    Long commentId = 1L;
    Long userId = 1L;

    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Updated comment content");

    when(commentService.doesCommentExist(commentId)).thenReturn(true);
    when(commentService.doesCommentBelongToUser(commentId, userId)).thenReturn(false);

    mockMvc.perform(put("/ads/{adId}/comments/{commentId}", 1L, commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testUpdateComment_AdClosed() throws Exception {
    Long commentId = 1L;
    Long userId = 1L;

    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setContent("Updated comment content");

    when(commentService.doesCommentExist(commentId)).thenReturn(true);
    when(commentService.doesCommentBelongToUser(commentId, userId)).thenReturn(true);
    when(adService.isAdClosed(anyLong())).thenReturn(true);

    mockMvc.perform(put("/ads/{adId}/comments/{commentId}", 1L, commentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commentRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testDeleteComment_Success() throws Exception {
    Long commentId = 1L;
    Long userId = 1L;

    when(commentService.doesCommentExist(commentId)).thenReturn(true);
    when(commentService.doesCommentBelongToUser(commentId, userId)).thenReturn(true);
    when(adService.isAdClosed(anyLong())).thenReturn(false);
    when(authService.getCurrentUserId()).thenReturn(userId);
    when(commentService.deleteComment(commentId)).thenReturn(true);

    mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1L, commentId))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  public void testDeleteComment_CommentNotFound() throws Exception {
    Long commentId = 1L;

    when(commentService.doesCommentExist(commentId)).thenReturn(false);

    mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1L, commentId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteComment_CommentDoesNotBelongToUser() throws Exception {
    Long commentId = 1L;
    Long userId = 1L;

    when(commentService.doesCommentExist(commentId)).thenReturn(true);
    when(commentService.doesCommentBelongToUser(commentId, userId)).thenReturn(false);

    mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1L, commentId))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testDeleteComment_AdClosed() throws Exception {
    Long commentId = 1L;
    Long userId = 1L;

    when(commentService.doesCommentExist(commentId)).thenReturn(true);
    when(commentService.doesCommentBelongToUser(commentId, userId)).thenReturn(true);
    when(adService.isAdClosed(anyLong())).thenReturn(true);

    mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1L, commentId))
        .andExpect(status().isForbidden());
  }
}
