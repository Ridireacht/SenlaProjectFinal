package com.senla.project.services;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.dto.MessageRequest;

public interface MessageService {

  ConversationResponse sendMessageWithConversationId(Long conversationId, MessageRequest messageRequest);
}
