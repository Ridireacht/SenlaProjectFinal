package com.senla.project.services.impl;

import com.senla.project.entities.Conversation;
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
  public List<Conversation> getAllConversations() {
    return null;
  }

  @Override
  public List<Conversation> getConversationsByUserId(Long id) {
    return null;
  }

  @Override
  public List<Conversation> getConversationsByAdId(Long id) {
    return null;
  }

  @Override
  public Conversation getConversation(Long id) {
    return null;
  }

  @Override
  public Conversation createConversation() {
    return null;
  }

  @Override
  public boolean deleteConversation(Long id) {
    return false;
  }
}
