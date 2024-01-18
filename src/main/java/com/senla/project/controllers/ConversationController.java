package com.senla.project.controllers;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.services.ConversationService;
import com.senla.project.services.UserService;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
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


  @GetMapping("/api/conversations")
  public List<ConversationResponse> getAllUserConversations() {
    return conversationService.getConversationsByUserId(getCurrentUserId());
  }

  @GetMapping("/api/conversations/{id}")
  public ConversationResponse getConversation(@PathVariable("id") Long id) {
    return conversationService.getConversation(id);
  }
  
  @PostMapping("/api/ads/{adId}/discuss")
  public ConversationResponse createConversation(@PathVariable("adId") Long adId) {
    return conversationService.createConversationByAdId(adId);
  }

  @DeleteMapping("/api/conversations/{id}")
  public boolean deleteConversation(@PathVariable("id") Long id) {
    return conversationService.deleteConversation(id);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
