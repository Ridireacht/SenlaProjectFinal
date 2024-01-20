package com.senla.project.services.impl;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.dto.MessageRequest;
import com.senla.project.repositories.MessageRepository;
import com.senla.project.services.MessageService;
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
