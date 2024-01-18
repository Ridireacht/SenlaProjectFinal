package com.senla.project.controllers;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.services.ConversationService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ConversationController {

  private final ConversationService conversationService;


  @GetMapping("/api/conversations")
  public List<ConversationResponse> getAllUserConversations() {
    return conversationService.getConversationsByUserId();
  }

  @GetMapping("/api/conversations/{id}")
  public ConversationResponse getConversation(@PathVariable("id") Long id) {
    return conversationService.getConversation(id);
  }

  @GetMapping("/api/ads/{adId}/conversation")
  public ConversationResponse getBuyerConversation(@PathVariable("adId") Long adId) {
    return conversationService.getBuyerConversationByAdId(adId);
  }

  @GetMapping("/api/ads/{adId}/conversations")
  public List<ConversationResponse> getSellerConversations(@PathVariable("adId") Long adId) {
    return conversationService.getSellerConversationsByAdId(adId);
  }

  @PostMapping("/api/ads/{adId}/conversation")
  public ConversationResponse createConversation(@PathVariable("adId") Long adId) {
    return conversationService.createConversationByAdId(adId);
  }

  @DeleteMapping("/api/conversations/{id}")
  public boolean deleteConversation(@PathVariable("id") Long id) {
    return conversationService.deleteConversation(id);
  }

}
