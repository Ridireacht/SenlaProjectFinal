package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.MessageRequest;
import com.senla.project.dto.MessageResponse;
import com.senla.project.entities.Message;
import com.senla.project.entities.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageMapperTest {

  @Autowired
  MessageMapper mapper;


  @Test
  public void testMapToMessageResponse() {
    User user = new User();
    user.setId(2L);

    Message message = new Message();
    message.setSender(user);
    message.setContent("testContent");
    message.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));

    MessageResponse expectedResponse = new MessageResponse();
    expectedResponse.setSenderId(2L);
    expectedResponse.setContent("testContent");
    expectedResponse.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));

    MessageResponse actualResponse = mapper.mapToMessageResponse(message);

    assertEquals(expectedResponse.getSenderId(), actualResponse.getSenderId());
    assertEquals(expectedResponse.getContent(), actualResponse.getContent());
    assertEquals(expectedResponse.getPostedAt(), actualResponse.getPostedAt());
  }

  @Test
  public void testMapToMessage() {
    MessageRequest messageRequest = new MessageRequest();
    messageRequest.setContent("testContent");

    Message expectedEntity = new Message();
    expectedEntity.setContent("testContent");

    Message actualEntity = mapper.mapToMessage(messageRequest);

    assertEquals(expectedEntity.getContent(), actualEntity.getContent());
  }
}
