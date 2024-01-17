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
  public List<ConversationResponse> getAllConversations() {
    return null;
  }

  @Override
  public List<ConversationResponse> getConversationsByUserId(Long id) {
    return null;
  }

  @Override
  public List<ConversationResponse> getConversationsByAdId(Long id) {
    return null;
  }

  @Override
  public ConversationResponse getConversation(Long id) {
    return null;
  }

  @Override
  public ConversationResponse createConversationByAdId(Long id) {
    return null;
  }

  @Override
  public boolean deleteConversation(Long id) {
    return false;
  }
}
