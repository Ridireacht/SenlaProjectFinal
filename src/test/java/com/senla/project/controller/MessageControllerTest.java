package com.senla.project.controller;


import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.project.config.SecurityConfig;
import com.senla.project.dto.request.MessageRequest;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.exception.GlobalExceptionHandler;
import com.senla.project.service.AuthService;
import com.senla.project.service.ConversationService;
import com.senla.project.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = MessageController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    })
@ContextConfiguration(classes = { MessageController.class, GlobalExceptionHandler.class })
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MessageService messageService;

  @MockBean
  private ConversationService conversationService;

  @MockBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  public void testSendMessageToConversation_Success() throws Exception {
    Long conversationId = 1L;
    Long userId = 1L;

    MessageRequest messageRequest = new MessageRequest();
    messageRequest.setContent("Test message content");

    ConversationResponse expectedResponse = new ConversationResponse();
    expectedResponse.setId(conversationId);
    expectedResponse.setAdId(1L);

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(conversationService.doesConversationExist(conversationId)).thenReturn(true);
    when(conversationService.doesConversationBelongToUser(conversationId, userId)).thenReturn(true);
    when(messageService.sendMessageToConversation(eq(userId), eq(conversationId), any(MessageRequest.class))).thenReturn(expectedResponse);

    mockMvc.perform(post("/conversations/{id}/messages", conversationId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(messageRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(conversationId.intValue())))
        .andExpect(jsonPath("$.adId", is(1)));
  }

  @Test
  public void testSendMessageToConversation_ConversationNotFound() throws Exception {
    Long conversationId = 1L;

    MessageRequest messageRequest = new MessageRequest();
    messageRequest.setContent("Test message content");

    when(conversationService.doesConversationExist(conversationId)).thenReturn(false);

    mockMvc.perform(post("/conversations/{id}/messages", conversationId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(messageRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testSendMessageToConversation_Forbidden() throws Exception {
    Long conversationId = 1L;
    Long userId = 1L;

    MessageRequest messageRequest = new MessageRequest();
    messageRequest.setContent("Test message content");

    when(conversationService.doesConversationExist(conversationId)).thenReturn(true);
    when(authService.getCurrentUserId()).thenReturn(userId);
    when(conversationService.doesConversationBelongToUser(conversationId, userId)).thenReturn(false);

    mockMvc.perform(post("/conversations/{id}/messages", conversationId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(messageRequest)))
        .andExpect(status().isForbidden());
  }
}
