package com.senla.project.service;

import com.senla.project.dto.request.MessageRequest;
import com.senla.project.dto.response.ConversationFullResponse;

public interface MessageService {

  ConversationFullResponse sendMessageToConversation(Long userId, Long conversationId, MessageRequest messageRequest);
}
