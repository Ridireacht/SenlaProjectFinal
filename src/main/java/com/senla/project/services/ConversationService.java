package com.senla.project.services;

import com.senla.project.entities.Conversation;
import java.util.List;

public interface ConversationService {

  List<Conversation> getAllConversations();

  List<Conversation> getConversationsByUserId(Long id);

  List<Conversation> getConversationsByAdId(Long id);

  Conversation getConversation(Long id);

  Conversation createConversation();

  boolean deleteConversation(Long id);
}
