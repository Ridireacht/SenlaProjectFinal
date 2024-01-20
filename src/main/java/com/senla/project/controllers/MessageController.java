package com.senla.project.controllers;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.dto.MessageRequest;
import com.senla.project.services.MessageService;
import com.senla.project.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MessageController {

  private final MessageService messageService;
  private final UserService userService;


  @PostMapping("/conversations/{id}/messages")
  public ConversationResponse sendMessage(@PathVariable("id") Long conversationId, MessageRequest messageRequest) {
    return messageService.sendMessageWithConversationId(getCurrentUserId(), conversationId, messageRequest);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
