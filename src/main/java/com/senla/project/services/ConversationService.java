package com.senla.project.services;

import com.senla.project.dto.ConversationResponse;
import java.util.List;

public interface ConversationService {

  List<ConversationResponse> getConversationsByUserId(Long userId);

  List<ConversationResponse> getSellerConversationsByAdId(Long adId);

  ConversationResponse getBuyerConversationByAdId(Long adId);

  ConversationResponse getConversation(Long id);

  ConversationResponse createConversationByAdId(Long adId);

  boolean deleteConversation(Long id);
}
