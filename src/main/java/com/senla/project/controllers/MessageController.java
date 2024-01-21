package com.senla.project.controllers;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.dto.MessageRequest;
import com.senla.project.services.ConversationService;
import com.senla.project.services.MessageService;
import com.senla.project.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MessageController {

  private final MessageService messageService;
  private final UserService userService;
  private final ConversationService conversationService;


  @PostMapping("/conversations/{id}/messages")
  public ResponseEntity<ConversationResponse> sendMessage(@PathVariable("id") Long conversationId, @RequestBody MessageRequest messageRequest) {
    if (!conversationService.doesConversationExist(conversationId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!conversationService.doesConversationBelongToUser(conversationId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(messageService.sendMessageWithConversationId(getCurrentUserId(), conversationId, messageRequest));
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
