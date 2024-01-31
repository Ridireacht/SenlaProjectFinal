package com.senla.project.service;

import com.senla.project.dto.response.ConversationFullResponse;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.dto.request.MessageRequest;

public interface MessageService {

  ConversationFullResponse sendMessageWithConversationId(Long userId, Long conversationId, MessageRequest messageRequest);
}
