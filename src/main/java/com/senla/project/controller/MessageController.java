package com.senla.project.controller;

import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.dto.request.MessageRequest;
import com.senla.project.service.ConversationService;
import com.senla.project.service.MessageService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Сообщение", description = "API для управления сообщениями")
@RestController
@AllArgsConstructor
public class MessageController {

  private final MessageService messageService;
  private final UserService userService;
  private final ConversationService conversationService;


  @PostMapping("/conversations/{id}/messages")
  public ResponseEntity<ConversationResponse> sendMessage(@PathVariable("id") Long conversationId, @Valid @RequestBody MessageRequest messageRequest) {
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
