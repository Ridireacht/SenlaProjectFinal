package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.senla.project.dto.request.MessageRequest;
import com.senla.project.dto.response.MessageResponse;
import com.senla.project.entity.Message;
import com.senla.project.entity.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.AUTO_CONFIGURED)
public class MessageMapperTest {

  @Autowired
  private MessageMapper messageMapper;


  @Test
  public void testMapToMessageResponse() {
    Message message = createMessage();
    MessageResponse messageResponse = messageMapper.mapToMessageResponse(message);

    assertEquals(message.getSender().getId(), messageResponse.getSenderId());
    assertEquals(message.getContent(), messageResponse.getContent());
    assertEquals(message.getPostedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), messageResponse.getPostedAt());
  }

  @Test
  public void testMapToMessage() {
    MessageRequest messageRequest = createMessageRequest();
    Message message = messageMapper.mapToMessage(messageRequest);

    assertNotNull(message);
    assertEquals(messageRequest.getContent(), message.getContent());
  }

  private Message createMessage() {
    Message message = new Message();
    message.setId(1L);
    message.setPostedAt(LocalDateTime.now());
    message.setContent("Test Content");

    User sender = new User();
    sender.setId(2L);
    message.setSender(sender);

    return message;
  }

  private MessageRequest createMessageRequest() {
    MessageRequest messageRequest = new MessageRequest();
    messageRequest.setContent("Test Content");
    return messageRequest;
  }
}
