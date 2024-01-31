package com.senla.project.service;

import com.senla.project.dto.request.MessageRequest;
import com.senla.project.dto.response.ConversationResponse;

public interface MessageService {

  ConversationResponse sendMessageToConversation(Long userId, Long conversationId, MessageRequest messageRequest);
}
