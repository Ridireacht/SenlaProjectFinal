package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.response.ConversationInfoResponse;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Conversation;
import com.senla.project.entity.Message;
import com.senla.project.entity.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = { ConversationMapperImpl.class, MessageMapper.class, MessageMapperImpl.class })
public class ConversationMapperTest {

  @Autowired
  private ConversationMapperImpl conversationMapper;


  @Test
  public void testMapToConversationInfoResponse() {
    Conversation conversation = createConversation();
    ConversationInfoResponse conversationInfoResponse = conversationMapper.mapToConversationInfoResponse(conversation);

    assertEquals(conversation.getId(), conversationInfoResponse.getId());
    assertEquals(conversation.getAd().getId(), conversationInfoResponse.getAdId());
    assertEquals(conversation.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), conversationInfoResponse.getUpdatedAt());
  }

  @Test
  public void testMapToConversationResponse() {
    Conversation conversation = createConversation();
    ConversationResponse conversationResponse = conversationMapper.mapToConversationResponse(conversation);

    assertEquals(conversation.getId(), conversationResponse.getId());
    assertEquals(conversation.getAd().getId(), conversationResponse.getAdId());
    assertEquals(conversation.getMessages().size(), conversationResponse.getMessages().size());
  }

  private Conversation createConversation() {
    Conversation conversation = new Conversation();
    conversation.setId(1L);
    conversation.setUpdatedAt(LocalDateTime.now());

    Ad ad = new Ad();
    ad.setId(2L);
    conversation.setAd(ad);

    User initiator = new User();
    initiator.setId(3L);
    conversation.setInitiator(initiator);

    User receiver = new User();
    receiver.setId(4L);
    conversation.setReceiver(receiver);

    Message message1 = new Message();
    message1.setId(5L);
    message1.setContent("Test Message 1");
    message1.setPostedAt(LocalDateTime.now());

    Message message2 = new Message();
    message2.setId(6L);
    message2.setContent("Test Message 2");
    message2.setPostedAt(LocalDateTime.now().plusHours(1));

    conversation.setMessages(Arrays.asList(message1, message2));

    return conversation;
  }
}