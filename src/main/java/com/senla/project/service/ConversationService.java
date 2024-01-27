package com.senla.project.service;

import com.senla.project.dto.response.ConversationResponse;
import java.util.List;

public interface ConversationService {

  Long getAdId(Long conversationId);

  List<ConversationResponse> getConversationsByUserId(Long userId);

  ConversationResponse getConversation(Long conversationId);

  ConversationResponse createConversationByAdId(Long userId, Long adId);

  boolean deleteConversation(Long conversationId);

  boolean doesConversationExist(Long conversationId);

  boolean doesConversationBelongToUser(Long conversationId, Long currentUserId);
}
