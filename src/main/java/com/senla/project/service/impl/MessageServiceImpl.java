package com.senla.project.service.impl;

import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.dto.request.MessageRequest;
import com.senla.project.repository.MessageRepository;
import com.senla.project.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;


  @Override
  public ConversationResponse sendMessageWithConversationId(Long userId, Long conversationId,
      MessageRequest messageRequest) {
    return null;
  }
}
