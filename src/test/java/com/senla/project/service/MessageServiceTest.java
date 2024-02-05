package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.MessageRequest;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Conversation;
import com.senla.project.entity.Message;
import com.senla.project.entity.User;
import com.senla.project.mapper.ConversationMapperImpl;
import com.senla.project.mapper.MessageMapperImpl;
import com.senla.project.repository.ConversationRepository;
import com.senla.project.repository.MessageRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.MessageServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { ConversationMapperImpl.class, MessageMapperImpl.class, MessageServiceImpl.class })
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

  @MockBean
  private MessageRepository messageRepository;

  @MockBean
  private ConversationRepository conversationRepository;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private MessageService messageService;


  @Test
  public void testSendMessageToConversation() {
    long senderId = 1L;
    long conversationId = 2L;

    User sender = createUser(senderId);
    MessageRequest messageRequest = createMessageRequest();
    Conversation conversation = createConversation(conversationId, senderId, 3L);
    Message message = createMessage(1L, senderId, conversationId);

    when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
    when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
    when(messageRepository.save(message)).thenReturn(message);
    when(conversationRepository.save(conversation)).thenReturn(conversation);

    ConversationResponse result = messageService.sendMessageToConversation(senderId, conversationId, messageRequest);

    assertNotNull(result);
    assertEquals(conversation.getId(), result.getId());
    assertEquals(conversation.getAd().getId(), result.getAdId());
  }

  private User createUser(long senderId) {
    User user = new User();
    user.setId(senderId);
    return user;
  }

  private Conversation createConversation(long conversationId, long initiatorId, long receiverId) {
    Conversation conversation = new Conversation();
    conversation.setId(conversationId);
    conversation.setAd(createAd());
    conversation.setInitiator(createUser(initiatorId));
    conversation.setReceiver(createUser(receiverId));
    return conversation;
  }

  private Ad createAd() {
    Ad ad = new Ad();
    ad.setId(5L);
    return ad;
  }

  private Message createMessage(long messageId, long senderId, long conversationId) {
    Message message = new Message();
    message.setId(messageId);
    message.setContent("Test Message");
    message.setSender(createUser(senderId));
    message.setConversation(createConversation(conversationId, senderId, 3L));
    return message;
  }

  private MessageRequest createMessageRequest() {
    MessageRequest messageRequest = new MessageRequest();
    messageRequest.setContent("Test Message");
    return messageRequest;
  }
}

