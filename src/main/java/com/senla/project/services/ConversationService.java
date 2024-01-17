package com.senla.project.services;

import com.senla.project.dto.ConversationResponse;
import java.util.List;

public interface ConversationService {

  List<ConversationResponse> getAllConversations();

  List<ConversationResponse> getConversationsByUserId(Long id);

  List<ConversationResponse> getConversationsByAdId(Long id);

  ConversationResponse getConversation(Long id);

  ConversationResponse createConversationByAdId(Long id);

  boolean deleteConversation(Long id);
}
