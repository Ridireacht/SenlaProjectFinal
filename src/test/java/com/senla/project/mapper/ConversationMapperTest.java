package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.dto.MessageResponse;
import com.senla.project.entities.Ad;
import com.senla.project.entities.Conversation;
import com.senla.project.entities.Message;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConversationMapperTest {

  private final ConversationMapper mapper = Mappers.getMapper(ConversationMapper.class);

  
  @Test
  public void testMapToConversationResponse() {
    Ad ad = new Ad();
    ad.setId(2L);

    ArrayList<Message> messages = new ArrayList<>();
    messages.add(new Message());
    messages.add(new Message());
    messages.get(0).setContent("testContent1");
    messages.get(1).setContent("testContent2");

    ArrayList<MessageResponse> messageResponses = new ArrayList<>();
    messageResponses.add(new MessageResponse());
    messageResponses.add(new MessageResponse());
    messageResponses.get(0).setContent("testContent1");
    messageResponses.get(1).setContent("testContent2");

    Conversation conversation = new Conversation();
    conversation.setId(1L);
    conversation.setAd(ad);
    conversation.setMessages(messages);

    ConversationResponse expectedResponse = new ConversationResponse();
    expectedResponse.setId(1L);
    expectedResponse.setAdId(2L);
    expectedResponse.setMessages(messageResponses);

    ConversationResponse actualResponse = mapper.mapToConversationResponse(conversation);

    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getAdId(), actualResponse.getAdId());
    assertEquals(expectedResponse.getMessages().size(), actualResponse.getMessages().size());
    assertEquals(expectedResponse.getMessages().get(0).getContent(), actualResponse.getMessages().get(0).getContent());
    assertEquals(expectedResponse.getMessages().get(1).getContent(), actualResponse.getMessages().get(1).getContent());
  }
}