package com.senla.project.service;

import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.dto.request.MessageRequest;

public interface MessageService {

  ConversationResponse sendMessageWithConversationId(Long userId, Long conversationId, MessageRequest messageRequest);
}
