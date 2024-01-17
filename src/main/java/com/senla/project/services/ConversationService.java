package com.senla.project.services;

import com.senla.project.entities.Conversation;
import java.util.List;

public interface ConversationService {

  List<Conversation> getAllConversations();

  List<Conversation> getConversationsByUserId();

  List<Conversation> getConversationsByAdId();

  Conversation getConversation();

  Conversation createConversation();

  boolean deleteConversation();
}
