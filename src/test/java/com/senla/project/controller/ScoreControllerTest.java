package com.senla.project.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.project.config.SecurityConfig;
import com.senla.project.dto.request.ScoreRequest;
import com.senla.project.exception.GlobalExceptionHandler;
import com.senla.project.service.AdService;
import com.senla.project.service.AuthService;
import com.senla.project.service.ScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = ScoreController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    })
@ContextConfiguration(classes = { ScoreController.class, GlobalExceptionHandler.class })
public class ScoreControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ScoreService scoreService;

  @MockBean
  private AdService adService;

  @MockBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  public void testSetScoreToAd_Success() throws Exception {
    Long adId = 1L;
    Long userId = 1L;
    ScoreRequest scoreRequest = new ScoreRequest();
    scoreRequest.setValue(4);

    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.isAdSoldToUser(adId, userId)).thenReturn(true);
    when(adService.isAdScored(adId)).thenReturn(false);
    when(authService.getCurrentUserId()).thenReturn(userId);
    when(scoreService.setScoreToAd(eq(userId), eq(adId), any(ScoreRequest.class))).thenReturn(true);

    mockMvc.perform(post("/ads/purchased/{id}", adId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(scoreRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  public void testSetScoreToAd_AdNotFound() throws Exception {
    Long adId = 1L;
    ScoreRequest scoreRequest = new ScoreRequest();
    scoreRequest.setValue(4);

    when(adService.doesAdExist(adId)).thenReturn(false);

    mockMvc.perform(post("/ads/purchased/{id}", adId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(scoreRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testSetScoreToAd_AdNotSoldToUser() throws Exception {
    Long adId = 1L;
    Long userId = 1L;
    ScoreRequest scoreRequest = new ScoreRequest();
    scoreRequest.setValue(4);

    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.isAdSoldToUser(adId, userId)).thenReturn(false);

    mockMvc.perform(post("/ads/purchased/{id}", adId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(scoreRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testSetScoreToAd_AdAlreadyScored() throws Exception {
    Long adId = 1L;
    Long userId = 1L;
    ScoreRequest scoreRequest = new ScoreRequest();
    scoreRequest.setValue(4);

    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.isAdSoldToUser(adId, userId)).thenReturn(true);
    when(adService.isAdScored(adId)).thenReturn(true);

    mockMvc.perform(post("/ads/purchased/{id}", adId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(scoreRequest)))
        .andExpect(status().isForbidden());
  }
}
