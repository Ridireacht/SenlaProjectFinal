package com.senla.project.service;

import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.dto.response.ConversationInfoResponse;
import java.util.List;

public interface ConversationService {

  long getAdId(long conversationId);

  List<ConversationInfoResponse> getConversationsOfUser(long userId);

  ConversationResponse getConversation(long conversationId);

  ConversationResponse createConversationByAd(long userId, long adId);

  boolean deleteConversation(long conversationId);

  boolean doesConversationExist(long conversationId);

  boolean doesConversationBelongToUser(long conversationId, long currentUserId);
}
