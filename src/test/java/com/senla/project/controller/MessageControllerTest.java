package com.senla.project.controller;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.senla.project.config.SecurityConfig;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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


  @Test
  public void testSendMessageToConversation() throws Exception {
    when(conversationService.doesConversationExist(anyLong())).thenReturn(true);
    when(conversationService.doesConversationBelongToUser(anyLong(), anyLong())).thenReturn(true);
    when(messageService.sendMessageToConversation(anyLong(), anyLong(), any())).thenReturn(new ConversationResponse());

    String requestBody = "{\"content\": \"Test message\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/conversations/1/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testSendMessageWithBlankContent() throws Exception {
    String requestBody = "{\"content\": \"\"}";

    mockMvc.perform(MockMvcRequestBuilders.post("/conversations/1/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Content must not be blank")));
  }
}
