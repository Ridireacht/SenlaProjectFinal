package com.senla.project.controller;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.service.AdService;
import com.senla.project.service.ConversationService;
import com.senla.project.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ConversationController {

  private final ConversationService conversationService;
  private final UserService userService;
  private final AdService adService;


  @GetMapping("/conversations")
  public List<ConversationResponse> getAllUserConversations() {
    return conversationService.getConversationsByUserId(getCurrentUserId());
  }

  @GetMapping("/conversations/{id}")
  public ResponseEntity<ConversationResponse> getConversation(@PathVariable("id") Long conversationId) {
    if (!conversationService.doesConversationExist(conversationId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!conversationService.doesConversationBelongToUser(conversationId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(conversationService.getConversation(conversationId));
  }
  
  @PostMapping("/ads/{adId}/discuss")
  public ResponseEntity<ConversationResponse> createConversation(@PathVariable("adId") Long adId) {
    if (!adService.doesAdExist(adId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Здесь, в отличие от прочих использований этой конструкции, не используется отрицание -
    // создатель объявления НЕ должен иметь возможности начать с собой переписку.
    if (adService.isAdAvailableForUser(adId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(conversationService.createConversationByAdId(getCurrentUserId(), adId));
  }

  @DeleteMapping("/conversations/{id}")
  public ResponseEntity<Boolean> deleteConversation(@PathVariable("id") Long conversationId) {
    if (!conversationService.doesConversationExist(conversationId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!conversationService.doesConversationBelongToUser(conversationId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(conversationService.deleteConversation(conversationId));
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }

}
