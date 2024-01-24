package com.senla.project.service;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.dto.MessageRequest;

public interface MessageService {

  ConversationResponse sendMessageWithConversationId(Long userId, Long conversationId, MessageRequest messageRequest);
}
