package com.senla.project.controllers;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.services.ConversationService;
import com.senla.project.services.UserService;
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


  @GetMapping("/conversations")
  public List<ConversationResponse> getAllUserConversations() {
    return conversationService.getConversationsByUserId(getCurrentUserId());
  }

  @GetMapping("/conversations/{id}")
  public ConversationResponse getConversation(@PathVariable("id") Long conversationId) {
    return conversationService.getConversation(conversationId);
  }
  
  @PostMapping("/ads/{adId}/discuss")
  public ConversationResponse createConversation(@PathVariable("adId") Long adId) {
    return conversationService.createConversationByAdId(getCurrentUserId(), adId);
  }

  @DeleteMapping("/conversations/{id}")
  public boolean deleteConversation(@PathVariable("id") Long conversationId) {
    return conversationService.deleteConversation(conversationId);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }

}
