package com.senla.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.senla.project.config.SecurityConfig;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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


  @Test
  public void testSetScoreToAd() throws Exception {
    when(adService.doesAdExist(anyLong())).thenReturn(true);
    when(adService.isAdSoldToUser(anyLong(), anyLong())).thenReturn(true);
    when(adService.isAdScored(anyLong())).thenReturn(false);
    when(scoreService.setScoreToAd(anyLong(), anyLong(), any())).thenReturn(true);

    String requestBody = "{\"value\": 5}";

    mockMvc.perform(MockMvcRequestBuilders.post("/ads/purchased/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk());
  }

  @Test
  public void testSetScoreToAdWithInvalidAd() throws Exception {
    when(adService.doesAdExist(anyLong())).thenReturn(false);

    String requestBody = "{\"value\": 5}";

    mockMvc.perform(MockMvcRequestBuilders.post("/ads/purchased/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testSetScoreToAdWithForbiddenUser() throws Exception {
    when(adService.doesAdExist(anyLong())).thenReturn(true);
    when(adService.isAdSoldToUser(anyLong(), anyLong())).thenReturn(false);

    String requestBody = "{\"value\": 5}";

    mockMvc.perform(MockMvcRequestBuilders.post("/ads/purchased/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testSetScoreToAdWithAlreadyScoredAd() throws Exception {
    when(adService.doesAdExist(anyLong())).thenReturn(true);
    when(adService.isAdSoldToUser(anyLong(), anyLong())).thenReturn(true);
    when(adService.isAdScored(anyLong())).thenReturn(true);

    String requestBody = "{\"value\": 5}";

    mockMvc.perform(MockMvcRequestBuilders.post("/ads/purchased/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isForbidden());
  }
}
