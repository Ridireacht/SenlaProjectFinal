package com.senla.project.services;

import com.senla.project.dto.ConversationResponse;
import java.util.List;

public interface ConversationService {

  List<ConversationResponse> getConversationsByUserId(Long userId);

  ConversationResponse getConversation(Long conversationId);

  ConversationResponse createConversationByAdId(Long userId, Long adId);

  boolean deleteConversation(Long conversationId);

  boolean doesConversationExist(Long conversationId);

  boolean doesConversationBelongToUser(Long conversationId, Long currentUserId);
}
