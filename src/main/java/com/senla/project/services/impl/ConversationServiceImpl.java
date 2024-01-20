package com.senla.project.services.impl;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.repositories.ConversationRepository;
import com.senla.project.services.ConversationService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConversationServiceImpl implements ConversationService {

  private final ConversationRepository conversationRepository;


  @Override
  public List<ConversationResponse> getConversationsByUserId(Long userId) {
    return null;
  }

  @Override
  public ConversationResponse getConversation(Long conversationId) {
    return null;
  }

  @Override
  public ConversationResponse createConversationByAdId(Long userId, Long adId) {
    return null;
  }

  @Override
  public boolean deleteConversation(Long conversationId) {
    return false;
  }

}
