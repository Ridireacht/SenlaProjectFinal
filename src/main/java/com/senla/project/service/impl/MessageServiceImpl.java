package com.senla.project.service.impl;

import com.senla.project.dto.request.MessageRequest;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.entity.Conversation;
import com.senla.project.entity.Message;
import com.senla.project.entity.User;
import com.senla.project.mapper.ConversationMapper;
import com.senla.project.mapper.MessageMapper;
import com.senla.project.repository.ConversationRepository;
import com.senla.project.repository.MessageRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.MessageService;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final ConversationRepository conversationRepository;
  private final UserRepository userRepository;

  private final ConversationMapper conversationMapper;
  private final MessageMapper messageMapper;


  @Transactional
  @Override
  public ConversationResponse sendMessageToConversation(long senderId, long conversationId,
      MessageRequest messageRequest) {
    User sender = userRepository.findById(senderId).get();

    Message message = messageMapper.mapToMessage(messageRequest);
    message.setSender(sender);
    message.setConversation(conversationRepository.findById(conversationId).get());
    message.setContent(messageRequest.getContent());
    message.setPostedAt(LocalDateTime.now());

    messageRepository.save(message);

    Conversation conversation = conversationRepository.findById(conversationId).get();
    conversation.setUpdatedAt(message.getPostedAt());
    conversation = conversationRepository.save(conversation);

    return conversationMapper.mapToConversationResponse(conversation);
  }

}
