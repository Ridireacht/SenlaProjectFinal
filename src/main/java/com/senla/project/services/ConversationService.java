package com.senla.project.services;

import com.senla.project.entities.Conversation;

public interface ConversationService {

  Conversation getConversation();

  Conversation createConversation();

  Conversation updateConversation();

  boolean deleteConversation();
}
