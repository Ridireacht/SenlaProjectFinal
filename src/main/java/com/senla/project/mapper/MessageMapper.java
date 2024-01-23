package com.senla.project.mapper;

import com.senla.project.dto.MessageRequest;
import com.senla.project.dto.MessageResponse;
import com.senla.project.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

  @Mapping(source = "sender.id", target = "senderId")
  MessageResponse mapToMessageResponse(Message message);

  Message mapToMessage(MessageRequest messageRequest);
}
