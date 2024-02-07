package com.senla.project.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.project.config.SecurityConfig;
import com.senla.project.dto.response.ConversationInfoResponse;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.exception.GlobalExceptionHandler;
import com.senla.project.service.AdService;
import com.senla.project.service.AuthService;
import com.senla.project.service.ConversationService;
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
    controllers = ConversationController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    })
@ContextConfiguration(classes = { ConversationController.class, GlobalExceptionHandler.class })
public class ConversationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ConversationService conversationService;

  @MockBean
  private AdService adService;

  @MockBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  public void testGetCurrentUserConversations_Success() throws Exception {
    Long userId = 1L;

    ConversationInfoResponse infoResponse1 = new ConversationInfoResponse();
    infoResponse1.setId(1L);
    infoResponse1.setAdId(1L);

    ConversationInfoResponse infoResponse2 = new ConversationInfoResponse();
    infoResponse2.setId(2L);
    infoResponse2.setAdId(2L);

    List<ConversationInfoResponse> conversationInfoResponses = new ArrayList<>();
    conversationInfoResponses.add(infoResponse1);
    conversationInfoResponses.add(infoResponse2);

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(conversationService.getConversationsOfUser(userId)).thenReturn(conversationInfoResponses);

    mockMvc.perform(get("/conversations"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].adId", is(1)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].adId", is(2)));
  }

  @Test
  public void testGetConversation_Success() throws Exception {
    Long conversationId = 1L;
    Long userId = 1L;

    ConversationResponse conversationResponse = new ConversationResponse();
    conversationResponse.setId(conversationId);
    conversationResponse.setAdId(1L);

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(conversationService.doesConversationExist(conversationId)).thenReturn(true);
    when(conversationService.doesConversationBelongToUser(conversationId, userId)).thenReturn(true);
    when(conversationService.getConversation(conversationId)).thenReturn(conversationResponse);

    mockMvc.perform(get("/conversations/{id}", conversationId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(conversationId.intValue())))
        .andExpect(jsonPath("$.adId", is(1)));
  }

  @Test
  public void testGetConversation_ConversationNotFound() throws Exception {
    Long conversationId = 1L;
    
    when(conversationService.doesConversationExist(conversationId)).thenReturn(false);

    mockMvc.perform(get("/conversations/{id}", conversationId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetConversation_Forbidden() throws Exception {
    Long conversationId = 1L;
    Long userId = 1L;

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(conversationService.doesConversationExist(conversationId)).thenReturn(true);
    when(conversationService.doesConversationBelongToUser(conversationId, userId)).thenReturn(false);

    mockMvc.perform(get("/conversations/{id}", conversationId))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testCreateConversationByAd_Success() throws Exception {
    Long adId = 1L;
    Long userId = 1L;

    ConversationResponse conversationResponse = new ConversationResponse();
    conversationResponse.setId(1L);
    conversationResponse.setAdId(adId);

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.doesAdBelongToUser(adId, userId)).thenReturn(false);
    when(adService.isAdClosed(adId)).thenReturn(false);
    when(adService.doesUserHaveConversationAlready(adId, userId)).thenReturn(false);
    when(conversationService.createConversationByAd(userId, adId)).thenReturn(conversationResponse);

    mockMvc.perform(post("/ads/{adId}/discuss", adId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.adId", is(adId.intValue())));
  }

  @Test
  public void testCreateConversationByAd_AdNotFound() throws Exception {
    Long adId = 1L;

    when(adService.doesAdExist(adId)).thenReturn(false);

    mockMvc.perform(post("/ads/{adId}/discuss", adId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testCreateConversationByAd_Forbidden_OwnAd() throws Exception {
    Long adId = 1L;
    Long userId = 1L;

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.doesAdBelongToUser(adId, userId)).thenReturn(true);

    mockMvc.perform(post("/ads/{adId}/discuss", adId))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testCreateConversationByAd_Forbidden_ClosedAd() throws Exception {
    Long adId = 1L;
    Long userId = 1L;

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.doesAdBelongToUser(adId, userId)).thenReturn(false);
    when(adService.isAdClosed(adId)).thenReturn(true);

    mockMvc.perform(post("/ads/{adId}/discuss", adId))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testCreateConversationByAd_Forbidden_UserHasActiveConversation() throws Exception {
    Long adId = 1L;
    Long userId = 1L;

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.doesAdBelongToUser(adId, userId)).thenReturn(false);
    when(adService.isAdClosed(adId)).thenReturn(false);
    when(adService.doesUserHaveConversationAlready(adId, userId)).thenReturn(true);

    mockMvc.perform(post("/ads/{adId}/discuss", adId))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testDeleteConversation_Success() throws Exception {
    Long conversationId = 1L;
    Long userId = 1L;

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(conversationService.doesConversationExist(conversationId)).thenReturn(true);
    when(conversationService.doesConversationBelongToUser(conversationId, userId)).thenReturn(true);
    when(conversationService.deleteConversation(conversationId)).thenReturn(true);

    mockMvc.perform(delete("/conversations/{id}", conversationId))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  public void testDeleteConversation_ConversationNotFound() throws Exception {
    Long conversationId = 1L;

    when(conversationService.doesConversationExist(conversationId)).thenReturn(false);

    mockMvc.perform(delete("/conversations/{id}", conversationId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteConversation_Forbidden() throws Exception {
    Long conversationId = 1L;
    Long userId = 1L;

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(conversationService.doesConversationExist(conversationId)).thenReturn(true);
    when(conversationService.doesConversationBelongToUser(conversationId, userId)).thenReturn(false);

    mockMvc.perform(delete("/conversations/{id}", conversationId))
        .andExpect(status().isForbidden());
  }
}
