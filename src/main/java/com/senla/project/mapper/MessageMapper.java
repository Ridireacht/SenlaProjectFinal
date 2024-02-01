package com.senla.project.mapper;

import com.senla.project.dto.request.MessageRequest;
import com.senla.project.dto.response.MessageResponse;
import com.senla.project.entity.Message;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = DateTimeFormatter.class)
public interface MessageMapper {

  @Mapping(source = "sender.id", target = "senderId")
  @Mapping(target = "postedAt", expression = "java(message.getPostedAt().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")))")
  MessageResponse mapToMessageResponse(Message message);

  Message mapToMessage(MessageRequest messageRequest);
}
